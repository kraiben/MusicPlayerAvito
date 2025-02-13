package com.gab.musicplayeravito.domain

import com.gab.musicplayeravito.domain.entities.CurrentTrackState
import com.gab.musicplayeravito.domain.entities.TrackInfo
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {

    fun getTracksNetwork(): StateFlow<List<TrackInfo>>

    suspend fun searchTracks(query: String)

    suspend fun loadNextData()

    suspend fun getTrackById(id: Long)

    suspend fun downloadTrack(trackInfo: TrackInfo)

    fun getDownloadedTracks(): StateFlow<TrackInfo>

    fun getCurrentTrack(): StateFlow<CurrentTrackState>

}