package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import com.gab.musicplayeravito.domain.entities.TrackInfo

sealed class SearchScreenState {

    data object Initial: SearchScreenState()

    data object Loading: SearchScreenState()

    class Tracks(val tracks: List<TrackInfo>): SearchScreenState()

}