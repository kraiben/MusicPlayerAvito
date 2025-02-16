package com.gab.musicplayeravito.ui.navigation

sealed class Screen(val route: String) {

    data object MusicSearchScreen : Screen(SEARCH_ROUTE)
    data object MusicDownloadedScreen : Screen(DOWNLOADED_ROUTE)
    data object AudioPlayerScreen : Screen(AUDIO_PLAYER_ROUTE) {

    }

    companion object {
        private const val KEY_TRACK = "track"

        const val SEARCH_ROUTE = "music_search_screen"
        const val DOWNLOADED_ROUTE = "music_downloaded_screen"
        const val AUDIO_PLAYER_ROUTE = "audio_player_screen"
    }

}
