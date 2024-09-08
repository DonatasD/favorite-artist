package com.donatasd.favoriteartist.artist;

import com.donatasd.favoriteartist.FavoriteArtistApplication;
import com.donatasd.favoriteartist.itunes.ITunesAlbum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FavoriteArtistApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ArtistControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void shouldCreateFavoriteArtist() throws Exception {
        var artist = generateTestArtist();

        ObjectMapper mapper = new ObjectMapper();
        var serializedStore = mapper.writeValueAsBytes(artist);

        mvc.perform(post("/api/artists").contentType(MediaType.APPLICATION_JSON).content(serializedStore))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindFavoriteArtists() throws Exception {
        var artist = artistRepository.save(ArtistFactory.create(generateTestArtist()));

        MvcResult result = mvc.perform(
                get("/api/artists")
                .param("userId", artist.getUserId().toString())
                .param("name", artist.getName())
        ).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        var artists = mapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Artist>>() {
        });

        assertEquals(1, artists.size());
        assertEquals(artist.getId(), artists.get(0).getId());
    }

    @Test
    void shouldFindTop5Albums() throws Exception {
        var artist = artistRepository.save(ArtistFactory.create(generateTestArtist()));

        MvcResult result = mvc.perform(
                get("/api/artists/" + artist.getId() + "/albums")
                        .param("userId", artist.getUserId().toString())
        ).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        var artists = mapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<ITunesAlbum>>() {
        });

        assertEquals(5, artists.size());
    }

    private static ArtistCreate generateTestArtist() {
        return new ArtistCreate("artistName", 123L, 529962L);
    }

}
