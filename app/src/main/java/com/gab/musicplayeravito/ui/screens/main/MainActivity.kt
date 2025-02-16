package com.gab.musicplayeravito.ui.screens.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gab.musicplayeravito.data.network.DeezerApiService
import com.gab.musicplayeravito.ui.MusicApplication
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.theme.MusicPlayerAvitoTheme
import com.gab.musicplayeravito.utils.GAB_CHECK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

//    @Inject
//    lateinit var exps: Exps

    private val component by lazy {
        (application as MusicApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        GAB_CHECK("__________________________________________________________")
        component.inject(this)
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MusicPlayerAvitoTheme {
                MainMusicScreen(viewModelFactory)
            }
        }
    }


}



class Exps @Inject constructor(
    private val apiService: DeezerApiService,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    operator fun invoke() {

    }

}