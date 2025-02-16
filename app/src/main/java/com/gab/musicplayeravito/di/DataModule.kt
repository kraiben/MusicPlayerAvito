package com.gab.musicplayeravito.di

import android.content.Context
import androidx.room.Room
import com.gab.musicplayeravito.data.filework.MusicDao
import com.gab.musicplayeravito.data.filework.MusicDataBase
import com.gab.musicplayeravito.data.network.DeezerApiService
import com.gab.musicplayeravito.data.network.utils.Endpoints
import com.gab.musicplayeravito.data.repository.MusicRepositoryImpl
import com.gab.musicplayeravito.domain.MusicRepository
import com.gab.musicplayeravito.ui.PlayerMusic
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindMusicRepisitory(repositoryImpl: MusicRepositoryImpl): MusicRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context): MusicDataBase {
            return Room.databaseBuilder(context, MusicDataBase::class.java, name = "database.db")
                .build()
        }

        @ApplicationScope
        @Provides
        fun providePlayerMusic(context: Context) = PlayerMusic.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideMusicDao(musicDataBase: MusicDataBase): MusicDao {
            return musicDataBase.musicDao()
        }

        @Provides
        @ApplicationScope
        fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                .build()

        @Provides
        @ApplicationScope
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(Endpoints.DEEZER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()


        @ApplicationScope
        @Provides
        fun provideApiService(retrofit: Retrofit): DeezerApiService = retrofit.create<DeezerApiService>()
    }
}