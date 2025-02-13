package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.domain.entities.TrackInfo

@Composable
fun TracksList(
    tracks: List<TrackInfo>,
    onSearchClickListener: (String) -> Unit = {},
    isDataDownloaded: Boolean = false,
    loadNext: () -> Unit = {},
    downloadIconClick: () -> Unit = {},
    deleteIconClick: () -> Unit = {},
    paddingValues: PaddingValues
) {
    var textField: String by remember { mutableStateOf("") }
    LazyColumn(modifier = Modifier) {

        item {
            Row(
                modifier = Modifier.padding(paddingValues)
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                TextField(
                    value = textField,
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    onValueChange = { textField = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                    )
                )
                Icon(
                    ImageVector.vectorResource(R.drawable.magnifying_glass),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).clickable {
                        onSearchClickListener(textField)
                    }
                )
            }
        }

        items(items = tracks, key = {it.id})  {track->
            TracksListElement(
                trackInfo = track,
                isDownloaded = track.isDownloaded)
        }

        item {

            if (!isDataDownloaded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else {
                SideEffect {
                    loadNext()
                }
            }

        }
    }
}