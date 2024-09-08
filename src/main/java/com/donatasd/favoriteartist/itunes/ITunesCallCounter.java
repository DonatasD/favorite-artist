package com.donatasd.favoriteartist.itunes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class ITunesCallCounter {

    private final CopyOnWriteArrayList<Instant> list = new CopyOnWriteArrayList<>();
    private final Predicate<Instant> predicate = instant -> Instant.now().minus(1, ChronoUnit.HOURS).isAfter(instant);

    public Integer increment() {
        list.removeIf(predicate);
        list.add(Instant.now());
        return list.size();
    }

    public Integer getCount() {
        list.removeIf(predicate);
        return list.size();
    }
}
