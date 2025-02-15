package com.gab.musicplayeravito.ui.screens.main

import androidx.lifecycle.ViewModel
import com.gab.musicplayeravito.domain.models.CurrentTrackState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val currentMusicState = flow<CurrentTrackState>{}.onStart {
        emit(CurrentTrackState.NoCurrentTrack)
    }

}