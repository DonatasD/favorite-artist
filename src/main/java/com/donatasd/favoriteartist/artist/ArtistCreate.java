package com.donatasd.favoriteartist.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistCreate(@NotBlank String name, @NotNull Long userId, @NotNull Long amgArtistId) {
}
