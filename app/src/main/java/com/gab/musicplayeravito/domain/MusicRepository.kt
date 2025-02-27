package com.gab.musicplayeravito.domain

import com.gab.musicplayeravito.domain.models.TrackInfoModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {

    fun getTracksNetwork(): StateFlow<List<TrackInfoModel>>

    suspend fun searchTracks(query: String)

    fun getAllDataIsLoadedEvent(): SharedFlow<List<TrackInfoModel>>

    suspend fun loadNextData()

    suspend fun setCurrentTrack(track: TrackInfoModel)

    fun getCurrentTrack(): StateFlow<TrackInfoModel?>

    suspend fun downloadTrack(trackInfo: TrackInfoModel)

    fun getDownloadedTracks(): StateFlow<TrackInfoModel>

    suspend fun nextTrack()

    suspend fun previousTrack()

    suspend fun pauseTrack()

    suspend fun startTrack()

}