package com.gab.musicplayeravito.data.filework.typeConverters

import androidx.room.TypeConverter
import com.gab.musicplayeravito.domain.models.AlbumInfoModel
import com.gab.musicplayeravito.domain.models.ArtistInfoModel
import com.google.gson.Gson

class MusicConverter {

    val gson = Gson()

    @TypeConverter
    fun convertFromArtis(artist: ArtistInfoModel): String {
        return gson.toJson(artist)
    }

    @TypeConverter
    fun convertToArtist(string: String): ArtistInfoModel {
        return gson.fromJson(string, ArtistInfoModel::class.java)
    }

    @TypeConverter
    fun convertFromAlbum(album: AlbumInfoModel): String {
        return gson.toJson(album)
    }

    @TypeConverter
    fun convertToAlbum(string: String): AlbumInfoModel {
        return gson.fromJson(string, AlbumInfoModel::class.java)
    }

}