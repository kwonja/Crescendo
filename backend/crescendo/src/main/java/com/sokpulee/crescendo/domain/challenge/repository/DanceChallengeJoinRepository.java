package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DanceChallengeJoinRepository extends JpaRepository<DanceChallengeJoin, Long> {

    Optional<DanceChallengeJoin> findByDanceChallengeAndUser(DanceChallenge danceChallenge, User user);

    @Query("SELECT dcj FROM DanceChallengeJoin dcj JOIN FETCH dcj.user WHERE dcj.id = :danceChallengeJoinId")
    Optional<DanceChallengeJoin> findByIdWithUser(@Param("danceChallengeJoinId") Long challengeJoinId);
}
