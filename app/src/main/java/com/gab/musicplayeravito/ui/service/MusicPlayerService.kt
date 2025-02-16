package com.gab.musicplayeravito.ui.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.domain.usecases.GetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.NextTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PauseTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PreviousTrackUseCase
import com.gab.musicplayeravito.domain.usecases.SetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.StartTrackUseCase
import com.gab.musicplayeravito.ui.MusicApplication
import com.gab.musicplayeravito.ui.PlayerMusic
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

const val PREV = "prev"
const val NEXT = "next"
const val PLAY_PAUSE = "play_pause"

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"

class MusicPlayerService : Service() {

    private val component by lazy {
        (application as MusicApplication).component
    }

    @Inject
    lateinit var getCurrentTrackUseCase: Provider<GetCurrentTrackUseCase>

    @Inject
    lateinit var setCurrentTrackUseCase: Provider<SetCurrentTrackUseCase>

    @Inject
    lateinit var nextTrackUseCase: Provider<NextTrackUseCase>

    @Inject
    lateinit var previousTrackUseCase: Provider<PreviousTrackUseCase>

    @Inject
    lateinit var startTrackUseCase: Provider<StartTrackUseCase>

    @Inject
    lateinit var pauseTrackUseCase: Provider<PauseTrackUseCase>

    @Inject
    lateinit var playerMusic: Provider<PlayerMusic>

    override fun onCreate() {
        component.inject(this)
        super.onCreate()

        playerMusic.get().addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    coroutineScope.launch {
                        onNextTrack()
                    }
                }
            }
        })
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val currentTrackState: StateFlow<CurrentTrackState> by lazy {
        getCurrentTrackUseCase.get()()
            .map {
                if (it == null) CurrentTrackState.NoCurrentTrack
                else CurrentTrackState.Track(it)
            }
            .stateIn(
                scope = coroutineScope,
                SharingStarted.Lazily,
                CurrentTrackState.NoCurrentTrack
            )
    }


    val binder = MusicBinder()

    inner class MusicBinder : Binder() {

        fun getService() = this@MusicPlayerService

        fun isPlaying() = this@MusicPlayerService.isPlaying

    }

    private var mediaPlayer = MediaPlayer()

    private val isPlaying = MutableStateFlow<Boolean>(false)

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        coroutineScope.launch {
            currentTrackState.collectLatest { trackState ->
                when (trackState) {
                    CurrentTrackState.Initial -> {}
                    CurrentTrackState.NoCurrentTrack -> {}
                    is CurrentTrackState.Track -> {
                        val track = trackState.track
                        if (!track.isPause)
                            playerMusic.get().play(track.previewUrl)
                        else
                            playerMusic.get().pause()

                        sendNotification(track)
                    }
                }
            }
        }

        intent?.let {
            when (intent.action) {
                PREV -> {
                    onPreviousTrack()
                }

                NEXT -> {
                    onNextTrack()
                }

                PLAY_PAUSE -> {
                    pauseTrack()
                }

                else -> {
                    startTrack()
                }
            }
        }

        return START_STICKY
    }

    fun onNextTrack() {
        coroutineScope.launch {
            nextTrackUseCase.get()()
            isPlaying.emit(true)
        }
    }

    fun onPreviousTrack() {
        coroutineScope.launch {
            previousTrackUseCase.get()()
            isPlaying.emit(true)
        }

    }

    fun pauseTrack() {
        coroutineScope.launch {
            pauseTrackUseCase.get()()
            isPlaying.emit(false)
        }
    }

    fun startTrack() {
        coroutineScope.launch {
            startTrackUseCase.get()()
            isPlaying.emit(true)
        }
    }


    private fun sendNotification(track: TrackInfoModel) {

        val session = MediaSessionCompat(this, "music")

        isPlaying.update { mediaPlayer.isPlaying }
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(session.sessionToken)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(track.title)
            .setContentText(track.artist.name)
            .addAction(R.drawable.ic_prev, "prev", createPrevPendingIntent())
            .addAction(
                if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                "play_pause",
                createPlayPausePendingIntent()
            )
            .addAction(R.drawable.ic_next, "next", createNextPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big_image))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

    fun createPrevPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = PREV
        }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = PLAY_PAUSE
        }
        return PendingIntent.getService(
            this, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = NEXT
        }
        return PendingIntent.getService(
            this, 2, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}