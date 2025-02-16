package com.gab.musicplayeravito.ui.screens.general

import com.gab.musicplayeravito.domain.models.TrackInfoModel

sealed class CurrentTrackState {

    data object Initial: CurrentTrackState()

    data object NoCurrentTrack: CurrentTrackState()

    class Track(val track: TrackInfoModel): CurrentTrackState()

}