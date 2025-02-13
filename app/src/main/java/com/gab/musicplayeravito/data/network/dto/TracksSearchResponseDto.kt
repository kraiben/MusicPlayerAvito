package com.gab.musicplayeravito.data.network.dto

import com.google.gson.annotations.SerializedName

data class TracksSearchResponseDto (
    @SerializedName("data") val response: List<TrackInfoDto>,
    @SerializedName("total") val total: Int
)