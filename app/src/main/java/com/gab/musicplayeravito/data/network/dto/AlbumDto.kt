package com.gab.musicplayeravito.data.network.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("cover_xl") val coverUrl: String
)