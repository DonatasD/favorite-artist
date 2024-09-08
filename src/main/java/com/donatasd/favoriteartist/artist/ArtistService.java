package com.donatasd.favoriteartist.artist;

import java.util.List;

import com.donatasd.favoriteartist.itunes.ITunesAlbum;
import com.donatasd.favoriteartist.itunes.ITunesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    private final ITunesService itunesService;

    private static final Logger logger = LoggerFactory.getLogger(ArtistService.class);

    public ArtistService(ArtistRepository artistRepository, ITunesService itunesService) {
        this.artistRepository = artistRepository;
        this.itunesService = itunesService;
    }

    public List<Artist> findAllByArtistCriteria(ArtistCriteria artistCriteria) {
        try {
            return artistCriteria.name()
                    .map((name) -> this.artistRepository.findAllByUserIdAndNameLikeIgnoreCase(
                            artistCriteria.userId(), "%" + artistCriteria.name().get() + "%")
                    )
                    .orElse(this.artistRepository.findAllByUserId(artistCriteria.userId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while searching for artists");
        }
    }

    public Artist create(ArtistCreate artist) {
        try {
            return this.artistRepository.save(ArtistFactory.create(artist));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the artist");
        }

    }

    public List<ITunesAlbum> findTop5Albums(Long id, Long userId) {
        try {
            var artist = this.artistRepository.findFirstByIdAndUserId(id, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
            return itunesService.findTop5AlbumsByAmgArtistId(artist.getAmgArtistId());
        } catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for top 5 albums");
        }
    }
}