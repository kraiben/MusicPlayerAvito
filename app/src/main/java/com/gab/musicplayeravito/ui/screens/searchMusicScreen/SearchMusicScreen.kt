package com.gab.musicplayeravito.ui.screens.searchMusicScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.navigation.NavigationState
import com.gab.musicplayeravito.ui.screens.general.CurrentTrackState
import com.gab.musicplayeravito.ui.screens.general.LoadingCircle
import com.gab.musicplayeravito.ui.screens.general.NavScaffold
import com.gab.musicplayeravito.ui.screens.general.TracksList
import com.gab.musicplayeravito.utils.GAB_CHECK

@Composable
fun SearchMusicScreen(
    viewModelFactory: ViewModelFactory,
    navigationState: NavigationState,
    currentTrackState: State<CurrentTrackState>,
    onTrackClickListener: (TrackInfoModel) -> Unit = {  },
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {}
) {
    val viewModel: SearchMusicViewModel = viewModel(factory = viewModelFactory)

    val searchScreenState = viewModel.screenState.collectAsState()

    NavScaffold(
        onNextClickListener = onNextClickListener,
        onPreviousClickListener = onPreviousClickListener,
        onStopClickListener = onStopClickListener,
        onStartClickListener = onStartClickListener,
        navigationState = navigationState,
        content = { modifier ->
            when (val currentState = searchScreenState.value) {
                is SearchScreenState.Tracks ->
                    TracksList(
                        modifier = modifier,
                    tracks = currentState.tracks,
                    loadNext = {
                        GAB_CHECK("SEARCH: Load next started")
                        viewModel.loadNextData()
                    },
                    allDataLoaded = {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(1.dp, color = Color.Black)
                        ){
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "All data loaded",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            )
                        }
                    },
                    tracksDownloadingState = currentState.dataLoadingState,
                    onTrackClickListener = onTrackClickListener
                )

                SearchScreenState.Initial -> {}
                SearchScreenState.Loading -> LoadingCircle(modifier = Modifier)

            }
        },
        onSearchClickListener = { query ->
            viewModel.searchTracks(query)
        },
        currentTrackState = currentTrackState
    )


}