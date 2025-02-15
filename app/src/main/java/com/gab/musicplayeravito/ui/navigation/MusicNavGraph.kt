package com.gab.musicplayeravito.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MusicNavGraph(
    navHostController: NavHostController,
    musicSearchScreenContent: @Composable () -> Unit,
    musicDownloadedScreenContent: @Composable () -> Unit,
    audioPlayerScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.MusicSearchScreen.route
    ) {
        composable(Screen.MusicSearchScreen.route) {
            musicSearchScreenContent()
        }
        composable(Screen.MusicDownloadedScreen.route) {
            musicDownloadedScreenContent()
        }
        composable(Screen.AudioPlayerScreen.route) {
            audioPlayerScreenContent()
        }
    }
}