package com.gab.musicplayeravito.data.network.dto

import com.google.gson.annotations.SerializedName

data class DefaultTracksDto(
    @SerializedName("tracks") val tracks: TracksSearchResponseDto
)