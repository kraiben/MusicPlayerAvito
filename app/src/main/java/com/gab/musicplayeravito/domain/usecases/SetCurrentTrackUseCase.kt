package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import javax.inject.Inject

class SetCurrentTrackUseCase @Inject constructor(
    private val repository: MusicRepository
) {

    suspend operator fun invoke(track: TrackInfoModel) {
        repository.setCurrentTrack(track)
    }

}