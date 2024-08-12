package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.LandmarkPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LandmarkPositionRepository extends JpaRepository<LandmarkPosition, Long> {
    List<LandmarkPosition> findByDanceChallenge(DanceChallenge danceChallenge);
}
