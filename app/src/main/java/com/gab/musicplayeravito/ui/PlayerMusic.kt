package com.gab.musicplayeravito.ui

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerMusic(context: Context) {

    private val _musicList: MutableStateFlow<MutableList<MediaItem>> =
        MutableStateFlow(mutableListOf())
    private val musicList = _musicList.asStateFlow()

    private var currentDuration: Long = 0L

    private val attributes = AudioAttributes.Builder()
        .setUsage(C.USAGE_MEDIA)
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .build()

    private val mediaPlayer by lazy {
        ExoPlayer.Builder(context)
            .setAudioAttributes(attributes, true)
            .build()
    }

    private var listener: Player.Listener? = null

    fun addListener(listener: Player.Listener) {
        mediaPlayer.addListener(listener)
        this.listener = listener
    }

    fun addTracks(vararg urls: String) {
        _musicList.update { mutableListOf() }

        _musicList.update { mediaItems ->
            mediaItems.addAll(urls.map { MediaItem.fromUri(it) })
            mediaPlayer.setMediaItems(mediaItems)
            listener?.let { mediaPlayer.addListener(it) }
            mediaItems
        }
    }

    fun play(url: String) {
        with(mediaPlayer) {
            setMediaItem(MediaItem.fromUri(url))
            seekTo(0)
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
