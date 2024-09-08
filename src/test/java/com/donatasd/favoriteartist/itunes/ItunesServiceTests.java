package com.donatasd.favoriteartist.itunes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

public class ItunesServiceTests {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ITunesCallCounter iTunesCallCounter;

    @InjectMocks
    private ITunesService iTunesService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    void shouldHandleWhenErrorThrown() {
        Long amgArtistId = 529962L;
        doThrow(new RuntimeException("error")).when(restTemplate).getForEntity(
                "https://itunes.apple.com/lookup?amgArtistId={amgArtistId}&entity=album&limit=5",
                String.class,
                Map.of("amgArtistId", amgArtistId.toString())
        );
        assertThrows(RestClientException.class, () -> iTunesService.findTop5AlbumsByAmgArtistId(amgArtistId));
   }
}
