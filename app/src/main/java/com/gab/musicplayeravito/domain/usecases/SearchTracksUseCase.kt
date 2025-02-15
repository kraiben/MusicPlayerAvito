package com.gab.musicplayeravito.domain.usecases

import com.gab.musicplayeravito.domain.MusicRepository
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(private val repository: MusicRepository) {

    suspend operator fun invoke(query: String) {
        return repository.searchTracks(query)
    }

}