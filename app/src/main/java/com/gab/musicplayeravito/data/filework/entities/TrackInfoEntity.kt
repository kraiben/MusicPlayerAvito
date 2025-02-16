package com.gab.musicplayeravito.data.filework.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gab.musicplayeravito.domain.models.AlbumInfoModel
import com.gab.musicplayeravito.domain.models.ArtistInfoModel

@Entity(tableName = "tracks")
data class TrackInfoEntity(

    @PrimaryKey val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: ArtistInfoModel,
    val album: AlbumInfoModel,

)
