package com.gab.musicplayeravito.ui.screens.main


import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.gab.musicplayeravito.ui.MusicApplication
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.service.MusicPlayerService
import com.gab.musicplayeravito.ui.theme.MusicPlayerAvitoTheme
import com.gab.musicplayeravito.utils.GAB_CHECK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val component by lazy {
        (application as MusicApplication).component
    }

    private val isPlaying = MutableStateFlow<Boolean>(false)
    private var service: MusicPlayerService? = null
    private var isBound = false

    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MusicPlayerService.MusicBinder).getService()
//            binder.setMusicList(songs)
            lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                    isPlaying.value = it
                }
            }
            lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                    isPlaying.value = it
                }
            }
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        GAB_CHECK("__________________________________________________________")
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerAvitoTheme {
                MainMusicScreen(viewModelFactory)
            }
        }
    }


}
