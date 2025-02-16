package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.gab.musicplayeravito.domain.models.TrackInfoModel

@Composable
fun TracksList(
    tracks: List<TrackInfoModel>,
    tracksDownloadingState: TracksDownloadState,
    loadNext: () -> Unit = {},
    allDataLoaded: @Composable () -> Unit = {},
    downloadIconClick: () -> Unit = {},
    deleteIconClick: () -> Unit = {},
    onTrackClickListener: (TrackInfoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {

        items(items = tracks, key = { "${it.id}_${it.artist.id}_${it.title}" }) { track ->
            TracksListElement(
                trackInfo = track,
                onDownloadIconClick = {
                    if (track.isDownloaded) {
                        deleteIconClick()
                    } else {
                        downloadIconClick()
                    }
                },
                onClick = onTrackClickListener
            )
        }

        item {
            when (tracksDownloadingState) {

                TracksDownloadState.DATA_IS_LOADING -> {
                    LoadingCircle()
                }

                TracksDownloadState.ALL_DATA_LOADED -> {
                    allDataLoaded()
                }

                TracksDownloadState.DATA_IS_NOT_LOADING -> {
                    SideEffect {
                        loadNext()
                    }
                }
            }
        }
    }
}