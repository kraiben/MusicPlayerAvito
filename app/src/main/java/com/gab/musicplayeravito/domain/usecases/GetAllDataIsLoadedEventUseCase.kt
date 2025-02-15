package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetAllDataIsLoadedEventUseCase @Inject constructor(private val repository: MusicRepository) {

    operator fun invoke(): SharedFlow<List<TrackInfoModel>> {
        return repository.getAllDataIsLoadedEvent()
    }

}