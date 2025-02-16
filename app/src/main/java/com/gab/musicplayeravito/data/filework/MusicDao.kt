package com.gab.musicplayeravito.data.filework

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gab.musicplayeravito.data.filework.entities.TrackInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackInfoEntity)

    @Query("SELECT * FROM tracks")
    fun getTracksInfo(): Flow<List<TrackInfoEntity>>

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun deleteTracks(id: Long): Unit

    @Query("DELETE FROM tracks")
    suspend fun clearTracksDb(): Unit

}