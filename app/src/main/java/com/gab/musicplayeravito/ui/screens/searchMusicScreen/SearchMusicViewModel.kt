package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gab.musicplayeravito.domain.usecases.GetAllDataIsLoadedEventUseCase
import com.gab.musicplayeravito.domain.usecases.GetTracksNetworkUseCase
import com.gab.musicplayeravito.domain.usecases.LoadNextDataUseCase
import com.gab.musicplayeravito.domain.usecases.SearchTracksUseCase
import com.gab.musicplayeravito.ui.screens.general.TracksDownloadState
import com.gab.musicplayeravito.utils.GAB_CHECK
import com.gab.musicplayeravito.utils.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val getTracksNetworkUseCase: GetTracksNetworkUseCase,
    private val searchTracksUseCase: SearchTracksUseCase,
    private val allDataIsLoadedEventUseCase: GetAllDataIsLoadedEventUseCase,
) : ViewModel() {

    init {
    }

    private val tracksFlow = getTracksNetworkUseCase()

    private val loadNextDataFlow = MutableSharedFlow<SearchScreenState>()
    private val allDataLoaded = allDataIsLoadedEventUseCase()

    private val allDataDownloadedFlow = flow {
        allDataLoaded.collect {
            GAB_CHECK("All tracks loaded")
            emit(
                SearchScreenState.Tracks(
                    it, TracksDownloadState.ALL_DATA_LOADED
                )
            )
        }
    }.shareIn(scope = viewModelScope, SharingStarted.Lazily)

    val screenState =
        tracksFlow
            .filter { it.isNotEmpty() }
            .map {
                SearchScreenState.Tracks(
                    it,
                    TracksDownloadState.DATA_IS_NOT_LOADING
                ) as SearchScreenState
            }

            .mergeWith(loadNextDataFlow)
            .mergeWith(allDataDownloadedFlow)
            .onEach {
                when (it) {
                    is SearchScreenState.Tracks -> {
                        GAB_CHECK(it.dataLoadingState)
                    }
                    SearchScreenState.Initial -> GAB_CHECK("STATE: Initial Search Music State")
                    SearchScreenState.Loading -> GAB_CHECK("STATE: Loading Search Music State")
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, SearchScreenState.Loading)

    fun loadNextData() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                SearchScreenState.Tracks(
                    tracks = tracksFlow.value, TracksDownloadState.DATA_IS_LOADING
                )
            )
            loadNextDataUseCase()
        }
    }


    fun searchTracks(query: String) {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                SearchScreenState.Loading
            )
            searchTracksUseCase(query)
        }
        GAB_CHECK(query + "|||||||||||||||||||||||||||||||||||||||||||||||||||||||")
    }

}