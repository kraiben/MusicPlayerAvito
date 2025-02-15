package com.gab.musicplayeravito.domain

import com.gab.musicplayeravito.domain.models.CurrentTrackState
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {

    fun getTracksNetwork(): StateFlow<List<TrackInfoModel>>

    suspend fun searchTracks(query: String)

    fun getAllDataIsLoadedEvent(): SharedFlow<List<TrackInfoModel>>

    suspend fun loadNextData()

    suspend fun getTrackById(id: Long)

    suspend fun downloadTrack(trackInfo: TrackInfoModel)

    fun getDownloadedTracks(): StateFlow<TrackInfoModel>

    fun getCurrentTrack(): StateFlow<CurrentTrackState>

}