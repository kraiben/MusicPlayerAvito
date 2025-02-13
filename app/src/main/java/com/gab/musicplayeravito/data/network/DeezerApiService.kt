package com.gab.musicplayeravito.data.network

import com.gab.musicplayeravito.data.network.dto.DefaultTracksDto
import com.gab.musicplayeravito.data.network.dto.TrackInfoDto
import com.gab.musicplayeravito.data.network.dto.TracksSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiService {

    @GET("search?")
    suspend fun searchMusic(
        @Query("q") query: String,
        @Query("index") index: Int,
    ): TracksSearchResponseDto

    @GET("track/{id}")
    suspend fun getTrackById(
        @Path("id") trackId: Long
    ): TrackInfoDto

    @GET("chart")
    suspend fun getDefaultTracks(): DefaultTracksDto
}