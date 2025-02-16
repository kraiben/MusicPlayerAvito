package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.domain.models.TrackInfoModel

@Composable
fun MusicPlayerMini(
    currentTrackState: State<CurrentTrackState>,
    modifier: Modifier = Modifier,
    onClick: (TrackInfoModel) -> Unit,
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {},
) {
    when (val currentState = currentTrackState.value) {
        CurrentTrackState.Initial -> {}
        CurrentTrackState.NoCurrentTrack -> {}
        is CurrentTrackState.Track -> {
            val trackInfo = currentState.track
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onClick(trackInfo) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(0.dp),
                border = BorderStroke(width = 1.dp, Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(4.dp),

                    ) {
                    AsyncImage(
                        model = if (trackInfo.album.coverUrl == "") R.drawable.megumindk
                        else trackInfo.album.coverUrl,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .weight(1f)
                            .wrapContentHeight()
                    ) {
                        Text(
                            trackInfo.title, fontSize = 20.sp, fontWeight = FontWeight.W600,
                            modifier = Modifier
                                .height(24.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            trackInfo.artist.name, fontSize = 16.sp, fontWeight = FontWeight.W200,
                            maxLines = 1,
                            modifier = Modifier
                                .wrapContentHeight()
                        )
                    }
                    Row(
                        modifier = modifier
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    onPreviousClickListener()
                                },
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.PlayArrow, contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    onStartClickListener()
                                }, tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    onNextClickListener()
                                },
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}