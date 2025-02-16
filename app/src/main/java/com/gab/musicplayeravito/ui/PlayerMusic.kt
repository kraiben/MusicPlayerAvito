package com.gab.musicplayeravito.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class PlayerMusic (context: Context) {

    private val _musicList: MutableStateFlow<MutableList<MediaItem>> =
        MutableStateFlow(mutableListOf())
    private val musicList = _musicList.asStateFlow()

    var currentTrackIndex = -1
        private set

    private var currentDuration: Long = 0L

    private val attributes = AudioAttributes.Builder()
        .setUsage(C.USAGE_MEDIA)
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .build()

    val mediaPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(attributes, true)
        .build()

    init {
        mediaPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED)
                    nextTrack()
            }
        })
    }

    fun nextTrack() {
        mediaPlayer.seekToNextMediaItem()
        if (currentTrackIndex > musicList.value.size - 1) currentTrackIndex++
    }

    fun previewTrack() {
        mediaPlayer.seekToPreviousMediaItem()
        if (currentTrackIndex > 0) currentTrackIndex--
    }

    fun addTracks(vararg urls: String) {
        _musicList.update { mediaItems ->
            mediaItems.addAll(urls.map { MediaItem.fromUri(it) })
            mediaPlayer.setMediaItems(mediaItems)
            mediaItems
        }
    }

    fun play(index: Int? = null) {
        if (index != null) currentTrackIndex = index

        with(mediaPlayer) {
            seekTo(currentTrackIndex, currentDuration)
            prepare()
            play()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            currentDuration = mediaPlayer.contentDuration
            mediaPlayer.pause()
        }
    }

    fun release() {
        mediaPlayer.release()
        INSTANCE = null
    }

    companion object {

        @Volatile
        private var INSTANCE: PlayerMusic? = null

        fun getInstance(context: Context): PlayerMusic {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlayerMusic(context).also { INSTANCE = it }
            }
        }
    }
}

fun ComponentActivity.getCurrentMusicProgress() = flow {
    val player = PlayerMusic.getInstance(this@getCurrentMusicProgress)
    emit(player.mediaPlayer.duration)
}

fun ComponentActivity.addTracks(vararg urls: String) {
    val player = PlayerMusic.getInstance(this)
    player.addTracks(*urls)
}

fun ComponentActivity.changePlayingStateOfIndex(index: Int) {
    val player = PlayerMusic.getInstance(this)
    val currentIndex = player.currentTrackIndex

    if (index == currentIndex) {
        if (player.mediaPlayer.isPlaying.not()) player.play(index)
        else player.pause()
    } else {
        player.play(index)
    }
}

fun ComponentActivity.nextTrack(index: Int? = null) {
    val player = PlayerMusic.getInstance(this)

    if (player.mediaPlayer.isPlaying) player.nextTrack()
    else player.play(index)

}

fun ComponentActivity.previewTrack() {
    val player = PlayerMusic.getInstance(this)
    player.previewTrack()
}

fun ComponentActivity.releasePlayer() {
    val player = PlayerMusic.getInstance(this)
    player.release()
}
