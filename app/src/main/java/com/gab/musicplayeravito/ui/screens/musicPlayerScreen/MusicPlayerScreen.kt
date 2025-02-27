package com.gab.musicplayeravito.ui.screens.musicPlayerScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import com.gab.musicplayeravito.utils.GAB_CHECK

@Composable
fun MusicPlayerScreen(
    currentTrackState: CurrentTrackState,
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {}
) {

    when (currentTrackState) {
        CurrentTrackState.NoCurrentTrack -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                PlayStopNextPreviousButtons(modifier = Modifier.align(Alignment.BottomCenter))
            }
        }

        is CurrentTrackState.Track -> {
            TrackPlayer(
                currentTrackState.track,
                onNextClickListener = onNextClickListener,
                onPreviousClickListener = onPreviousClickListener,
                onStopClickListener = onStopClickListener,
                onStartClickListener = onStartClickListener
            )
        }

        else -> {}
    }

}

@Composable
fun TrackPlayer(
    track: TrackInfoModel,
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        AsyncImage(
            model = if ("https" !in track.album.coverUrl) R.drawable.megumindk
            else track.album.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = track.title,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 36.sp,
            lineHeight = 40.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = track.artist.name,
                modifier = Modifier,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = ImageVector.vectorResource(
                    if (track.isDownloaded) R.drawable.trash_svgrepo
                    else R.drawable.download_icon_filled
                ),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        PlayStopNextPreviousButtons(
            onNextClickListener = onNextClickListener,
            onPreviousClickListener = onPreviousClickListener,
            onStopClickListener = onStopClickListener,
            onStartClickListener = onStartClickListener,
            isPaused = track.isPause
        )

        Spacer(modifier = Modifier)
    }
}

@Composable
fun PlayStopNextPreviousButtons(
    isPaused: Boolean = true,
    modifier: Modifier = Modifier,
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    onPreviousClickListener()
                }, tint = Color.Black
        )

        AnimatedContent(isPaused) {
            if (it) {
                Icon(
                    Icons.Default.PlayArrow, contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            onStartClickListener()
                        }, tint = Color.Black
                )
            } else {
                Icon(
                    Icons.Default.Add, contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            onStopClickListener()
                        }, tint = Color.Black
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForward, contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    onNextClickListener()
                }, tint = Color.Black
        )
    }
}