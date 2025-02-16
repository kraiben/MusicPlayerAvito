package com.gab.musicplayeravito.ui.screens.main

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.domain.usecases.GetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.NextTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PauseTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PreviousTrackUseCase
import com.gab.musicplayeravito.domain.usecases.SetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.StartTrackUseCase
import com.gab.musicplayeravito.ui.PlayerMusic
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import com.gab.musicplayeravito.ui.service.MusicPlayerService
import com.gab.musicplayeravito.utils.GAB_CHECK
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    private val setCurrentTrackUseCase: SetCurrentTrackUseCase,
    private val nextTrackUseCase: NextTrackUseCase,
    private val previousTrackUseCase: PreviousTrackUseCase,
    private val startTrackUseCase: StartTrackUseCase,
    private val pauseTrackUseCase: PauseTrackUseCase,
    private val playerMusic: PlayerMusic,
    private val context: Context
) : ViewModel() {

    val currentTrackState = getCurrentTrackUseCase()
        .map {
            if (it == null) CurrentTrackState.NoCurrentTrack
            else CurrentTrackState.Track(it)
        }
        .stateIn(scope = viewModelScope, SharingStarted.Lazily, CurrentTrackState.NoCurrentTrack)

    private var service: MusicPlayerService? = null


    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MusicPlayerService.MusicBinder).getService()
//            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
//            isBound = false
        }
    }

    init {



        viewModelScope.launch {
            currentTrackState.collectLatest { trackState ->
                when (trackState) {
                    CurrentTrackState.Initial -> {}
                    CurrentTrackState.NoCurrentTrack -> {}
                    is CurrentTrackState.Track -> {
                        val track = trackState.track
                        if (!track.isPause)
                            playerMusic.play(track.previewUrl)
                        else
                            playerMusic.pause()
                    }
                }
            }
        }
        setupService()

    }

    private fun setupService() {
        val intent = Intent(context, MusicPlayerService::class.java)
        context.startService(intent)
        context.bindService(intent, connection, BIND_AUTO_CREATE)
    }

    fun setCurrentTrack(track: TrackInfoModel) {
        viewModelScope.launch {
            setCurrentTrackUseCase(track)
        }
    }

    fun onNextTrack() {
//        viewModelScope.launch {
//            nextTrackUseCase()
//        }
        service?.onNextTrack()
    }

    fun onPreviousTrack() {
//        viewModelScope.launch {
//            previousTrackUseCase()
//        }
        service?.onPreviousTrack()
    }


    fun pauseTrack() {
//        viewModelScope.launch {
//            pauseTrackUseCase()
//        }
        service?.pauseTrack()
    }

    fun startTrack() {
//        viewModelScope.launch {
//            startTrackUseCase()
//        }

        service?.startTrack()
    }

}