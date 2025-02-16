package com.gab.musicplayeravito.data

import com.gab.musicplayeravito.data.network.dto.AlbumDto
import com.gab.musicplayeravito.data.network.dto.ArtistDto
import com.gab.musicplayeravito.data.network.dto.TrackInfoDto
import com.gab.musicplayeravito.domain.models.AlbumInfoModel
import com.gab.musicplayeravito.domain.models.ArtistInfoModel
import com.gab.musicplayeravito.domain.models.TrackInfoModel
import javax.inject.Inject

class MusicMapper @Inject constructor() {


    fun mapTrackInfoDtoIntoTrackInfo(trackInfoDto: TrackInfoDto, indexInList: Int) = TrackInfoModel(
        id = trackInfoDto.id,
        title = trackInfoDto.title,
        previewUrl = trackInfoDto.previewUrl,
        artist = mapArtistDtoIntoArtistInfo(trackInfoDto.artistDto),
        album = mapAlbumDtoIntoAlbumInfo(trackInfoDto.albumDto),
        indexInList = indexInList
    )

    private fun mapArtistDtoIntoArtistInfo(artistDto: ArtistDto) = ArtistInfoModel(
        id = artistDto.id,
        name = artistDto.name,
        pictureUrl = artistDto.artistPictureUrl
    )

    private fun mapAlbumDtoIntoAlbumInfo(albumDto: AlbumDto) = AlbumInfoModel(
        id = albumDto.id,
        title = albumDto.title,
        coverUrl = albumDto.coverUrl
    )
}