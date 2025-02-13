package com.gab.musicplayeravito.domain.entities

data class TrackInfo(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: ArtistInfo,
    val album: AlbumInfo,
    val isDownloaded: Boolean = false
)
