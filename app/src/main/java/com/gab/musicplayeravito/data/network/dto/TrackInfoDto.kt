package com.gab.musicplayeravito.data.network.dto

import com.google.gson.annotations.SerializedName

data class TrackInfoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("preview") val previewUrl: String,
    @SerializedName("artist") val artistDto: ArtistDto,
    @SerializedName("album") val albumDto: AlbumDto
)
