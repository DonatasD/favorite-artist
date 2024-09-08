package com.donatasd.favoriteartist.artist;


import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record ArtistCriteria(@NotNull Long userId, Optional<String> name) {
}
