package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.navigation.NavigationState
import com.gab.musicplayeravito.ui.screens.general.LoadingCircle
import com.gab.musicplayeravito.ui.screens.general.NavScaffold
import com.gab.musicplayeravito.ui.screens.general.TracksList

@Composable
fun SearchMusicScreen(
    viewModelFactory: ViewModelFactory,
    navigationState: NavigationState,
) {
    val viewModel: SearchMusicViewModel = viewModel(factory = viewModelFactory)

    val searchScreenState = viewModel.screenState.collectAsState(SearchScreenState.Initial)

    NavScaffold(navigationState) { paddingValues ->

        when (val currentState = searchScreenState.value) {
            SearchScreenState.Initial -> {}
            SearchScreenState.Loading -> LoadingCircle()
            is SearchScreenState.Tracks -> TracksList(
                paddingValues = paddingValues,
                tracks = currentState.tracks,
                loadNext = { viewModel.loadNextData() },
                onSearchClickListener = {query ->
                    viewModel.searchTracks(query)
                }
            )
        }

    }


}