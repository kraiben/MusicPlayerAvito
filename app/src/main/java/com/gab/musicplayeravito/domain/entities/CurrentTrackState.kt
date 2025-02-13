package com.gab.musicplayeravito.domain.entities

sealed class CurrentTrackState {

    data object Initial: CurrentTrackState()

    data object NoCurrentTrack: CurrentTrackState()

    class CurrentTrack(val track: TrackInfo): CurrentTrackState()

}