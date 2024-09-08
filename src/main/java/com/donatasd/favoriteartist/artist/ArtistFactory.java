package com.donatasd.favoriteartist.artist;

public class ArtistFactory {

    static Artist create(ArtistCreate artistCreate) {
        var artist = new Artist();
        artist.setAmgArtistId(artistCreate.amgArtistId());
        artist.setName(artistCreate.name());
        artist.setUserId(artistCreate.userId());
        return artist;
    }
}
