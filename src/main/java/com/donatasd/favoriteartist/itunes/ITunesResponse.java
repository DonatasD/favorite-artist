package com.donatasd.favoriteartist.itunes;

import java.util.List;

public record ITunesResponse<T>(Integer resultCount, List<T> results) {}
