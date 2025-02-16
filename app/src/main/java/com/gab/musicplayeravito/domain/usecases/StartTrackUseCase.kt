package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import javax.inject.Inject

class StartTrackUseCase @Inject constructor(private val repository: MusicRepository) {

    suspend operator fun invoke() {
        return repository.startTrack()
    }

}