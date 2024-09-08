package com.donatasd.favoriteartist.itunes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ITunesService {

    private final ITunesCallCounter callCounter;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final static Integer CACHE_THRESHOLD = 50;

    private final static Logger logger = LoggerFactory.getLogger(ITunesService.class);

    public ITunesService(ITunesCallCounter callCounter, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.callCounter = callCounter;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /*
     Cache and update cache until we have reach certain call count to itunes api. Reuse cache after that if possible
     reuse cache. If entry does not exist in cache call itunes.
     */
    @Cacheable(cacheNames = "albums", condition = "#root.target.getCount()>=#root.target.getCacheThreshold()")
    public List<ITunesAlbum> findTop5AlbumsByAmgArtistId(Long amgArtistId) {
        try {
            final String url = "https://itunes.apple.com/lookup?amgArtistId={amgArtistId}&entity=album&limit=5";

            Map<String, String> params = Map.of("amgArtistId", amgArtistId.toString());

            var response = this.restTemplate.getForEntity(url, String.class, params);
            ITunesResponse<ITunesAlbum> itunesAlbumResponse = this.objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            var result = itunesAlbumResponse.results().stream().filter(iTunesAlbum -> iTunesAlbum.wrapperType().equals("collection")).toList();
            var counter = this.callCounter.increment();
            logger.debug("Current call to iTunes api number: {}", counter);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RestClientException("Could not find top 5 albums", e);
        }
    }

    public Integer getCount() {
        return this.callCounter.getCount();
    }

    public Integer getCacheThreshold() {
        return CACHE_THRESHOLD;
    }
}