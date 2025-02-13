package com.gab.musicplayeravito.data

import com.gab.musicplayeravito.data.network.dto.AlbumDto
import com.gab.musicplayeravito.data.network.dto.ArtistDto
import com.gab.musicplayeravito.data.network.dto.TrackInfoDto
import com.gab.musicplayeravito.domain.entities.AlbumInfo
import com.gab.musicplayeravito.domain.entities.ArtistInfo
import com.gab.musicplayeravito.domain.entities.TrackInfo
import javax.inject.Inject

class MusicMapper @Inject constructor() {


    fun mapTrackInfoDtoIntoTrackInfo(trackInfoDto: TrackInfoDto) = TrackInfo(
        id = trackInfoDto.id,
        title = trackInfoDto.title,
        previewUrl = trackInfoDto.previewUrl,
        artist = mapArtistDtoIntoArtistInfo(trackInfoDto.artistDto),
        album = mapAlbumDtoIntoAlbumInfo(trackInfoDto.albumDto)
    )

    private fun mapArtistDtoIntoArtistInfo(artistDto: ArtistDto) = ArtistInfo(
        id = artistDto.id,
        name = artistDto.name,
        pictureUrl = artistDto.artistPictureUrl
    )

    private fun mapAlbumDtoIntoAlbumInfo(albumDto: AlbumDto) = AlbumInfo(
        id = albumDto.id,
        title = albumDto.title,
        coverUrl = albumDto.coverUrl
    )
}