package com.gab.musicplayeravito.data.repository

import com.gab.musicplayeravito.data.MusicMapper
import com.gab.musicplayeravito.data.filework.MusicDao
import com.gab.musicplayeravito.data.network.DeezerApiService
import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.utils.GAB_CHECK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val mapper: MusicMapper,
    private val apiService: DeezerApiService,
    private val musicDao: MusicDao,
) : MusicRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _trackList = mutableListOf<TrackInfoModel>()
    private val trackList: List<TrackInfoModel> get() = _trackList.toList()

    private var currentTrackIndex: Int = 0
    private val currentTrackState = MutableStateFlow<TrackInfoModel?>(null)

    private val queryState = MutableStateFlow<String?>(null)
    private var searchStartIndex = START_SEARCH_INDEX_DEFAULT

    private val isAllDataDownloadedEvent = MutableSharedFlow<List<TrackInfoModel>>()

    private val loadNextEvent = MutableSharedFlow<Unit>(replay = 1)

    private var currentTrackPlaylist = listOf<TrackInfoModel>()

    private val tracksLoaded = flow<List<TrackInfoModel>> {
        loadNextEvent.emit(Unit)

        loadNextEvent.collect {
            val startFrom = searchStartIndex

            if (startFrom == START_SEARCH_INDEX_DEFAULT) {
                setDefaultTrackList()
                searchStartIndex = 0
                isAllDataDownloadedEvent.emit(trackList)
                return@collect
            }
            val query = queryState.value
            if (query == null) {
                emit(trackList)
                return@collect
            }

            val response = apiService.searchMusic(query, index = startFrom).response
                .mapIndexed { index, trackInfoDto ->
                    mapper.mapTrackInfoDtoIntoTrackInfo(
                        trackInfoDto,
                        index
                    )
                }

            GAB_CHECK("${response.size} ____________________________________________________")
            val trackIds = trackList.map { it.id }
            val tracksToAdd = response.filter { element ->
                trackIds.count { id ->
                    id == element.id
                } < 1
            }
            _trackList.addAll(tracksToAdd)
            if (response.size < 25) {
                isAllDataDownloadedEvent.emit(trackList)
                return@collect
            }
            searchStartIndex += INDEX_INCREASE
            emit(trackList)
            GAB_CHECK("Data loaded")
        }//GAB_CHECK
    }.retry {
        GAB_CHECK("Track Loaded Flow Catched Exception")
        delay(3000)
        true
    }.stateIn(coroutineScope, SharingStarted.Lazily, listOf())

    override fun getCurrentTrack(): StateFlow<TrackInfoModel?> = currentTrackState

    override fun getTracksNetwork(): StateFlow<List<TrackInfoModel>> = tracksLoaded

    override suspend fun searchTracks(query: String) {
        GAB_CHECK("SEARCH: Search Started")
        searchStartIndex = 0
        _trackList.clear()
        queryState.emit(query)
        loadNextEvent.emit(Unit)
    }

    override suspend fun loadNextData() {
        loadNextEvent.emit(Unit)
    }

    override fun getAllDataIsLoadedEvent(): SharedFlow<List<TrackInfoModel>> =
        isAllDataDownloadedEvent

    override suspend fun setCurrentTrack(track: TrackInfoModel) {
        val id = track.id
        val selectedTrack = trackList.find { it.id == id }
            ?: throw RuntimeException("Track Not In List")
        currentTrackIndex = track.indexInList
        currentTrackState.emit(
            selectedTrack
        )
    }

    override suspend fun previousTrack() {
        if (trackList.isEmpty()) return

        val tracksCnt = trackList.size
        if (currentTrackIndex != 0) {
            currentTrackIndex--
            currentTrackState.emit(trackList[currentTrackIndex])
        } else {
            currentTrackState.emit(trackList.last())
            currentTrackIndex = tracksCnt - 1
        }
    }

    override suspend fun nextTrack() {
        if (trackList.isEmpty()) return

        val tracksCnt = trackList.size
        if (currentTrackIndex < tracksCnt - 1) {
            currentTrackIndex++
            currentTrackState.emit(trackList[currentTrackIndex])
        } else {
            currentTrackState.emit(trackList.first())
            currentTrackIndex = 0
        }
    }

    override suspend fun pauseTrack() {
        currentTrackState.update {
            it?.copy(isPause = true)
        }
    }

    override suspend fun startTrack() {
        currentTrackState.update {
            it?.copy(isPause = false)
        }
    }

    override suspend fun downloadTrack(trackInfo: TrackInfoModel) {
        TODO("Not yet implemented")
    }

    override fun getDownloadedTracks(): StateFlow<TrackInfoModel> {
        TODO("Not yet implemented")
    }

    private suspend fun setDefaultTrackList() {
        val defaultChart = apiService.getDefaultTracks()
        _trackList.addAll(
            defaultChart.tracks.response.mapIndexed { index, trackInfoDto ->
                mapper.mapTrackInfoDtoIntoTrackInfo(trackInfoDto, index)
            }
        )
    }
}

private const val START_SEARCH_INDEX_DEFAULT = -1
private const val INDEX_INCREASE = 25