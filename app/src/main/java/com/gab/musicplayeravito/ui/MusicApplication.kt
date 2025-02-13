package com.gab.musicplayeravito.ui

import android.app.Application
import com.gab.musicplayeravito.di.ApplicationComponent
import com.gab.musicplayeravito.di.DaggerApplicationComponent

class MusicApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}