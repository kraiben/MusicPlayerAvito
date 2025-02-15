package com.gab.musicplayeravito.di

import androidx.lifecycle.ViewModel
import com.gab.musicplayeravito.ui.screens.main.MainViewModel
import com.gab.musicplayeravito.ui.screens.searchMusicScreen.SearchMusicViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SearchMusicViewModel::class)
    @Binds
    fun bindSearchMusicViewModel(viewModel: SearchMusicViewModel): ViewModel

}