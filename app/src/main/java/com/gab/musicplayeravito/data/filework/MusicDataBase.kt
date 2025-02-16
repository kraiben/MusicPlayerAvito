package com.gab.musicplayeravito.data.filework

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gab.musicplayeravito.data.filework.entities.TrackInfoEntity
import com.gab.musicplayeravito.data.filework.typeConverters.MusicConverter


@Database(entities = [TrackInfoEntity::class], version = 1)
@TypeConverters(MusicConverter::class)
abstract class MusicDataBase: RoomDatabase() {
    abstract fun musicDao(): MusicDao
}