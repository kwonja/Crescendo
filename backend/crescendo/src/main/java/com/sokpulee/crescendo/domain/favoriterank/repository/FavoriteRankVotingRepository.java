package com.sokpulee.crescendo.domain.favoriterank.repository;

import com.sokpulee.crescendo.domain.favoriterank.entity.FavoriteRankVoting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRankVotingRepository extends JpaRepository<FavoriteRankVoting, Long> {

    Optional<FavoriteRankVoting> findByFavoriteRankIdAndUserId(Long favoriteRankId, Long userId);
    void deleteByFavoriteRankIdAndUserId(Long favoriteRankId, Long userId);
}
