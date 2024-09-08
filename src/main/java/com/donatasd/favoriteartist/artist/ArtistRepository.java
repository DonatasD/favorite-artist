package com.donatasd.favoriteartist.artist;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {

    List<Artist> findAllByUserIdAndNameLikeIgnoreCase(Long userId, String name);

    List<Artist> findAllByUserId(Long userId);

    Optional<Artist> findFirstByIdAndUserId(Long id, Long userId);

}
