package com.donatasd.favoriteartist.artist;

import com.donatasd.favoriteartist.itunes.ITunesAlbum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public List<Artist> findAllByCriteria(@Valid ArtistCriteria artistCriteria) {
        return artistService.findAllByArtistCriteria(artistCriteria);
    }

    @PostMapping()
    public Artist create(@Valid @RequestBody ArtistCreate artist) {
        return artistService.create(artist);
    }

    @GetMapping("/{id}/albums")
    public List<ITunesAlbum> findTopAlbumsByArtistId(@PathVariable Long id, @Valid @NotNull Long userId) {
        return artistService.findTop5Albums(id, userId);
    }

}