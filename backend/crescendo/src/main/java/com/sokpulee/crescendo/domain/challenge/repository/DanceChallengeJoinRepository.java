package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DanceChallengeJoinRepository extends JpaRepository<DanceChallengeJoin, Long> {

    Optional<DanceChallengeJoin> findByDanceChallengeAndUser(DanceChallenge danceChallenge, User user);
}
