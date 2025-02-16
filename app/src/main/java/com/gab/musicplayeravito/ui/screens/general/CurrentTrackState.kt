package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.runtime.Stable
import com.gab.musicplayeravito.domain.models.TrackInfoModel

@Stable
sealed class CurrentTrackState {

    data object Initial: CurrentTrackState()

    data object NoCurrentTrack: CurrentTrackState()

    @Stable
    class Track(val track: TrackInfoModel): CurrentTrackState()

}