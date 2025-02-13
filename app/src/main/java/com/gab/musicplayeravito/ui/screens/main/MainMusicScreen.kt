package com.gab.musicplayeravito.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.domain.entities.CurrentTrackState
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.navigation.MusicNavGraph
import com.gab.musicplayeravito.ui.navigation.rememberNavigationState
import com.gab.musicplayeravito.ui.screens.searchMusicScreen.SearchMusicScreen

@Composable
fun MainMusicScreen(viewModelFactory: ViewModelFactory) {

    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
    val navigationState = rememberNavigationState()
    val currentMusicState = viewModel.currentMusicState.collectAsState(CurrentTrackState.Initial)
    MusicNavGraph(
        navHostController = navigationState.navHostController,
        musicSearchScreenContent = { SearchMusicScreen(viewModelFactory, navigationState) },
        musicDownloadedScreenContent = {},
        audioPlayerScreenContent = {},
    )
}

