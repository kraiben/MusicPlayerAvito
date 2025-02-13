package com.gab.musicplayeravito.data.repository

import com.gab.musicplayeravito.data.MusicMapper
import com.gab.musicplayeravito.data.network.DeezerApiService
import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.entities.CurrentTrackState
import com.gab.musicplayeravito.domain.entities.TrackInfo
import com.gab.musicplayeravito.utils.pointLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val mapper: MusicMapper,
    private val apiService: DeezerApiService,
) : MusicRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _trackList = mutableListOf<TrackInfo>()
    private val trackList: List<TrackInfo> get() = _trackList.toList()

    private val currentTrackState =
        MutableStateFlow(CurrentTrackState.NoCurrentTrack as CurrentTrackState)

    private val queryState = MutableStateFlow(" ")
    private var next = START_SEARCH_INDEX

    private val loadNextEvent = MutableSharedFlow<Unit>(replay = 1)

    private val tracksLoaded = flow<List<TrackInfo>> {
        loadNextEvent.emit(Unit)
        loadNextEvent.collect {
            pointLog(2)
            val startFrom = next

            if (startFrom == START_SEARCH_INDEX) {
                pointLog(3)
                setDefaultTrackList()
                next = 0
                emit(trackList)
            } else {
                pointLog(4)
                val query = queryState.value
                val response = apiService.searchMusic(query, index = startFrom).response
                _trackList.addAll(response.map { mapper.mapTrackInfoDtoIntoTrackInfo(it) })
                next += INDEX_INCREASE
                emit(trackList)
            }
        }
    }.retry {
        delay(3000)
        true
    }.stateIn(coroutineScope, SharingStarted.Lazily, listOf())

    override fun getCurrentTrack(): StateFlow<CurrentTrackState> = currentTrackState

    override fun getTracksNetwork(): StateFlow<List<TrackInfo>> = tracksLoaded

    override suspend fun searchTracks(query: String) {
        next = 0
        _trackList.clear()
        queryState.emit(query)
        loadNextEvent.emit(Unit)
    }


    override suspend fun loadNextData() {
        loadNextEvent.emit(Unit)
    }

    override suspend fun getTrackById(id: Long) {
        val track = apiService.getTrackById(id)
        currentTrackState.emit(
            CurrentTrackState.CurrentTrack(mapper.mapTrackInfoDtoIntoTrackInfo(track))
        )
    }

    override suspend fun downloadTrack(trackInfo: TrackInfo) {
        TODO("Not yet implemented")
    }

    override fun getDownloadedTracks(): StateFlow<TrackInfo> {
        TODO("Not yet implemented")
    }

    private suspend fun setDefaultTrackList() {
        val defaultChart = apiService.getDefaultTracks()
        pointLog(defaultChart.toString())
        _trackList.addAll(
            defaultChart.tracks.response.map {
                mapper.mapTrackInfoDtoIntoTrackInfo(it)
            }
        )
    }

    companion object {
        private const val START_SEARCH_INDEX = -1
        private const val INDEX_INCREASE = 25

    }
}