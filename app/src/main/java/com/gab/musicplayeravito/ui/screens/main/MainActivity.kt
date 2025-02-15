package com.gab.musicplayeravito.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gab.musicplayeravito.data.network.DeezerApiService
import com.gab.musicplayeravito.ui.MusicApplication
import com.gab.musicplayeravito.ui.ViewModelFactory
import com.gab.musicplayeravito.ui.theme.MusicPlayerAvitoTheme
import com.gab.musicplayeravito.utils.GAB_CHECK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

//        exps()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerAvitoTheme {
                MainMusicScreen(viewModelFactory)
            }
        }
    }
}


class Exps @Inject constructor(
    private val apiService: DeezerApiService
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    operator fun invoke() {
        coroutineScope.launch {
            val indexList1 = getIndexResponse(0)
            val indexList2 = getIndexResponse(25)
            val indexList3 = getIndexResponse(50)

//            for (i in 0..24) {
//                pointLog( "${indexList1[i]}|||||${indexList2[i]}|||||${indexList3[i]}")
//            }
            for (i in indexList1) {
                if (i in indexList2) {
                    GAB_CHECK("1 $i")
                }
            }
            for (i in indexList2) {
                if (i in indexList3) {
                    GAB_CHECK("2 $i")
                }
            }
            for (i in indexList3) {
                if (i in indexList1) {
                    GAB_CHECK("3 $i")
                }
            }
        }
    }

    private suspend fun getIndexResponse(index: Int): List<Long> {
        val result = apiService.searchMusic(index = index, query = "I")
        GAB_CHECK(result.total.toString() + index.toString())
        return result.response.map{ it.id}
    }
}