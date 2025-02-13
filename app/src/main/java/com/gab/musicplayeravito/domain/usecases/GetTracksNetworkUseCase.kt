package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.entities.TrackInfo
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetTracksNetworkUseCase @Inject constructor(private val repository: MusicRepository) {

    operator fun invoke(): StateFlow<List<TrackInfo>> {
        return repository.getTracksNetwork()
    }

}