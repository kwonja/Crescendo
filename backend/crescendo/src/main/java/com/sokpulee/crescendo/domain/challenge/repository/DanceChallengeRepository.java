package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DanceChallengeRepository extends JpaRepository<DanceChallenge, Long> {
}
