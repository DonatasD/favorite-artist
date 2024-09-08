package com.donatasd.favoriteartist.itunes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ITunesAlbum(@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String wrapperType, String collectionName, Long amgArtistId) {}
