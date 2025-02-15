package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.ui.screens.general.TracksDownloadState

sealed class SearchScreenState {

    data object Initial: SearchScreenState()

    data object Loading: SearchScreenState()

    data class Tracks(
        val tracks: List<TrackInfoModel>,
        val dataLoadingState: TracksDownloadState
    ): SearchScreenState()

}