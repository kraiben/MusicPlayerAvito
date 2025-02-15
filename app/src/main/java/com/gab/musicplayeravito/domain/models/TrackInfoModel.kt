package com.gab.musicplayeravito.domain.models

data class TrackInfoModel(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: ArtistInfoModel,
    val album: AlbumInfoModel,
    val isDownloaded: Boolean = false
)
