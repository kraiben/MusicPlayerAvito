package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetCurrentTrackUseCase @Inject constructor(private val repository: MusicRepository) {

    operator fun invoke(): SharedFlow<TrackInfoModel> {
        return repository.getCurrentTrack()
    }

}