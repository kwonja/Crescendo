package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DanceChallengeRepository extends JpaRepository<DanceChallenge, Long> {

    @Query("SELECT dc FROM DanceChallenge dc JOIN FETCH dc.user WHERE dc.id = :danceChallengeId")
    Optional<DanceChallenge> findByIdWithUser(@Param("danceChallengeId") Long challengeId);
}
