package com.gab.musicplayeravito.di

import android.content.Context
import com.gab.musicplayeravito.ui.screens.main.MainActivity
import com.gab.musicplayeravito.ui.service.MusicPlayerService
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ViewModelModule::class,
        DataModule::class,
        ServiceModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(musicPlayerService: MusicPlayerService)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }

}