package com.gab.musicplayeravito.ui.screens.main

import androidx.lifecycle.ViewModel
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
    private val playerMusic: PlayerMusic
) : ViewModel() {

    val currentTrackState = getCurrentTrackUseCase()
        .map {
            if (it == null) CurrentTrackState.NoCurrentTrack
            else CurrentTrackState.Track(it)
        }
        .stateIn(scope = viewModelScope, SharingStarted.Lazily, CurrentTrackState.NoCurrentTrack)

    init {
        playerMusic.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    viewModelScope.launch {
                        nextTrackUseCase()
                    }
                }
            }

        })

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
    }

    fun setCurrentTrack(track: TrackInfoModel) {
        viewModelScope.launch {
            setCurrentTrackUseCase(track)
        }
    }

    fun onNextTrack() {
        viewModelScope.launch {
            nextTrackUseCase()
        }
    }

    fun onPreviousTrack() {
        viewModelScope.launch {
            previousTrackUseCase()
        }
    }

    fun pauseTrack() {
        viewModelScope.launch {
            pauseTrackUseCase()
        }
    }

    fun startTrack() {
        viewModelScope.launch {
            startTrackUseCase()
        }
    }

}