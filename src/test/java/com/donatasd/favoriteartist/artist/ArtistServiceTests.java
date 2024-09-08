package com.donatasd.favoriteartist.artist;

import com.donatasd.favoriteartist.itunes.ITunesAlbum;
import com.donatasd.favoriteartist.itunes.ITunesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class ArtistServiceTests {

    @Mock
    ITunesService itunesService;

    @Mock
    ArtistRepository artistRepository;

    @InjectMocks
    ArtistService artistService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    void shouldCreate() {
        var artistCreate = generateTestArtistCreate();
        doReturn(generateTestArtistEntity(artistCreate)).when(artistRepository).save(any(Artist.class));
        var result = artistService.create(artistCreate);
        assertEquals(artistCreate.name(), result.getName());
        assertEquals(artistCreate.amgArtistId(), result.getAmgArtistId());
        assertEquals(artistCreate.userId(), result.getUserId());
    }

    @Test
    void shouldHandleExceptionWhenCreating() {
        var artistCreate = generateTestArtistCreate();
        doThrow(new RuntimeException()).when(artistRepository).save(any(Artist.class));
        assertThrows(ResponseStatusException.class, () -> artistService.create(artistCreate));
    }

    @Test
    void shouldFindTop5Albums() {
        var artist = generateTestArtistEntity(generateTestArtistCreate());
        List<ITunesAlbum> albums = List.of();
        doReturn(albums).when(itunesService).findTop5AlbumsByAmgArtistId(artist.getAmgArtistId());
        doReturn(Optional.of(artist)).when(artistRepository).findFirstByIdAndUserId(artist.getId(), artist.getUserId());
        var result = artistService.findTop5Albums(artist.getId(), artist.getUserId());
        assertTrue(Arrays.deepEquals(new List[]{albums}, new List[]{result}));
    }

    @Test
    void shouldHandleExceptionForFindTop5AlbumsWhenArtistNotFound() {
        doReturn(Optional.empty()).when(artistRepository).findFirstByIdAndUserId(any(Long.class), any(Long.class));
        assertThrows(ResponseStatusException.class, () -> artistService.findTop5Albums(1L, 123L));
    }

    @Test
    void shouldHandleExceptionForFindTop5AlbumsWhenItunesServiceFails() {
        var artist = generateTestArtistEntity(generateTestArtistCreate());
        doThrow(new RuntimeException()).when(itunesService).findTop5AlbumsByAmgArtistId(artist.getAmgArtistId());
        doReturn(Optional.of(artist)).when(artistRepository).findFirstByIdAndUserId(artist.getId(), artist.getUserId());
        assertThrows(ResponseStatusException.class, () -> artistService.findTop5Albums(artist.getId(), artist.getUserId()));
    }

    @Test
    void shouldSearchForArtistsByName() {
        var artistCriteria = genereateTestArtistCriteria();
        var artists = List.of(generateTestArtistEntity(generateTestArtistCreate()));
        doReturn(artists).when(artistRepository).findAllByUserIdAndNameLikeIgnoreCase(artistCriteria.userId(), "%" + artistCriteria.name().get() + "%");
        var result = artistService.findAllByArtistCriteria(artistCriteria);
        assertTrue(Arrays.deepEquals(new List[]{artists}, new List[]{result}));
    }

    @Test
    void shouldSearchForArtistsWhenNameIsNotProvided() {
        var artistCriteria = generateTestArtistCriteriaWithoutName();
        var artists = List.of(generateTestArtistEntity(generateTestArtistCreate()));
        doReturn(artists).when(artistRepository).findAllByUserId(artistCriteria.userId());
        var result = artistService.findAllByArtistCriteria(artistCriteria);
        assertTrue(Arrays.deepEquals(new List[]{artists}, new List[]{result}));
    }

    @Test
    void shouldHandleExceptionWhenSearchingForArtists() {
        var artistCriteria = genereateTestArtistCriteria();
        doThrow(new RuntimeException()).when(artistRepository).findAllByUserIdAndNameLikeIgnoreCase(artistCriteria.userId(), "%" + artistCriteria.name().get() + "%");
        assertThrows(ResponseStatusException.class ,() -> artistService.findAllByArtistCriteria(artistCriteria));
    }



    private static ArtistCreate generateTestArtistCreate() {
        return new ArtistCreate("artistName", 123L, 529962L);
    }

    private static Artist generateTestArtistEntity(ArtistCreate artistCreate) {
        var artist = new Artist();
        artist.setId(1L);
        artist.setName(artistCreate.name());
        artist.setAmgArtistId(artistCreate.amgArtistId());
        artist.setUserId(artistCreate.userId());
        return artist;
    }

    private static ArtistCriteria genereateTestArtistCriteria() {
        return new ArtistCriteria(123L, Optional.of("artistName"));
    }

    private static ArtistCriteria generateTestArtistCriteriaWithoutName() {
        return new ArtistCriteria(123L, Optional.empty());
    }
}
