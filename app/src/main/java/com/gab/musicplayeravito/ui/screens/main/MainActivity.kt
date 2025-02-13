package com.gab.musicplayeravito.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gab.musicplayeravito.ui.MusicApplication
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.theme.MusicPlayerAvitoTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as MusicApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerAvitoTheme {
                MainMusicScreen(viewModelFactory)
            }
        }
    }
}


class Exps() {
}