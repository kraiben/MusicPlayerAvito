package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.domain.models.TrackInfoModel

@Composable
fun TracksListElement(
    trackInfo: TrackInfoModel,
    onDownloadIconClick: () -> Unit = {},
    onClick: () -> Unit = {},
    isDownloaded: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(width = 1.dp, Color.Black)
    ) {
        val icon = if (!isDownloaded) R.drawable.download_icon_filled
        else R.drawable.trash_svgrepo
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            AsyncImage(
                model = if (trackInfo.album.coverUrl == "") R.drawable.megumindk
                else trackInfo.album.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    trackInfo.title, fontSize = 24.sp, fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
                Text(
                    trackInfo.artist.name, fontSize = 16.sp, fontWeight = FontWeight.W200,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
            Icon(
                contentDescription = null,
                imageVector = ImageVector.vectorResource(icon),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDownloadIconClick() }
            )
        }
    }

}