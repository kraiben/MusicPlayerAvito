package com.gab.musicplayeravito.domain.models

data class TrackInfoModel(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: ArtistInfoModel,
    val album: AlbumInfoModel,
    val isDownloaded: Boolean = false,
    val indexInList: Int = 0,
    var isPause: Boolean = false
)
