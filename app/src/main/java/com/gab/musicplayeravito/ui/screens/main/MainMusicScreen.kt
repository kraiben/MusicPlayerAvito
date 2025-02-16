package com.gab.musicplayeravito.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.navigation.MusicNavGraph
import com.gab.musicplayeravito.ui.navigation.rememberNavigationState
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import com.gab.musicplayeravito.ui.screens.musicPlayerScreen.MusicPlayerScreen
import com.gab.musicplayeravito.ui.screens.searchMusicScreen.SearchMusicScreen

@Composable
fun MainMusicScreen(viewModelFactory: ViewModelFactory) {

    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
    val navigationState = rememberNavigationState()
    val currentTrackState by viewModel.currentTrackState.collectAsStateWithLifecycle()

    MusicNavGraph(
        navHostController = navigationState.navHostController,
        musicSearchScreenContent = {
            SearchMusicScreen(
                viewModelFactory,
                navigationState,
                onTrackClickListener = { track ->
                    viewModel.setCurrentTrack(track)
                    navigationState.navigateToPlayer()
                },
                currentTrackState = currentTrackState,
                onNextClickListener = { viewModel.onNextTrack() },
                onPreviousClickListener = { viewModel.onPreviousTrack() },
                onStopClickListener = { viewModel.pauseTrack() },
                onStartClickListener = { viewModel.startTrack() }
                )
        },
        musicDownloadedScreenContent = {},
        audioPlayerScreenContent = {
            MusicPlayerScreen(
                currentTrackState = currentTrackState,
                onNextClickListener = { viewModel.onNextTrack() },
                onPreviousClickListener = { viewModel.onPreviousTrack() },
                onStopClickListener = { viewModel.pauseTrack() },
                onStartClickListener = { viewModel.startTrack() }
            )
        },
    )
}

