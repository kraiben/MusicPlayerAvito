package com.gab.musicplayeravito.ui.screens.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.navigation.MusicNavGraph
import com.gab.musicplayeravito.ui.navigation.rememberNavigationState
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import com.gab.musicplayeravito.ui.screens.musicPlayerScreen.MusicPlayerScreen
import com.gab.musicplayeravito.ui.screens.searchMusicScreen.SearchMusicScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainMusicScreen(viewModelFactory: ViewModelFactory) {

    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
    val navigationState = rememberNavigationState()
    val currentTrackState by viewModel.currentTrackState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
//    val state = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    val notificationPermissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { wasGranted ->

            if (wasGranted) {
            }
        }


//    notificationPermissionState.launchPermissionRequest()

    if (notificationPermissionState.status.isGranted.not()) {
        LaunchedEffect(notificationPermissionState) {
            when (notificationPermissionState.status) {
                is PermissionStatus.Denied -> {
                    notificationPermissionState.launchPermissionRequest()
                }
                PermissionStatus.Granted -> {}
            }
        }
    }


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

