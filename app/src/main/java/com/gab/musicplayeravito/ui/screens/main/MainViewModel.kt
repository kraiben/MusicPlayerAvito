package com.gab.musicplayeravito.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.domain.usecases.GetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.NextTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PauseTrackUseCase
import com.gab.musicplayeravito.domain.usecases.PreviousTrackUseCase
import com.gab.musicplayeravito.domain.usecases.SetCurrentTrackUseCase
import com.gab.musicplayeravito.domain.usecases.StartTrackUseCase
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import kotlinx.coroutines.flow.SharingStarted
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
) : ViewModel() {

    val currentTrackState = getCurrentTrackUseCase().map {
        CurrentTrackState.Track(it)
    }.stateIn(scope = viewModelScope, SharingStarted.Lazily, CurrentTrackState.NoCurrentTrack)

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