package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gab.musicplayeravito.domain.usecases.GetTracksNetworkUseCase
import com.gab.musicplayeravito.domain.usecases.LoadNextDataUseCase
import com.gab.musicplayeravito.domain.usecases.SearchTracksUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val getTracksNetworkUseCase: GetTracksNetworkUseCase,
    private val searchTracksUseCase: SearchTracksUseCase,
) : ViewModel() {

    val screenState = getTracksNetworkUseCase().map { SearchScreenState.Tracks(it) }
        .onStart {
            SearchScreenState.Loading
        }

    fun loadNextData() {
        viewModelScope.launch {
            loadNextDataUseCase()
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            searchTracksUseCase(query)
        }
    }

}