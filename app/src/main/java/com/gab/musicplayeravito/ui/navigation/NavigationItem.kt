package com.gab.musicplayeravito.ui.navigation
import com.gab.musicplayeravito.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val iconResId: Int
) {

    data object Search : NavigationItem(
        screen = Screen.MusicSearchScreen,
        titleResId = R.string.search_icon_title,
        iconResId = R.drawable.magnifying_glass
    )

    data object Downloaded: NavigationItem(
        screen = Screen.MusicDownloadedScreen,
        titleResId = R.string.downloaded_icon_title,
        iconResId = R.drawable.save_icon_125167
    )
}