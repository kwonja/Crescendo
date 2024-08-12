package com.sokpulee.crescendo.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LandmarkPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landmark_position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dance_challenge_id")
    private DanceChallenge danceChallenge;

    private int frameNumber;

    private int landmarkIndex;

    private double x;

    private double y;

    private double z;

    private double visibility;

    public LandmarkPosition(DanceChallenge danceChallenge, int frameNumber, int landmarkIndex, double x, double y, double z, double visibility) {
        this.danceChallenge = danceChallenge;
        this.frameNumber = frameNumber;
        this.landmarkIndex = landmarkIndex;
        this.x = x;
        this.y = y;
        this.z = z;
        this.visibility = visibility;
    }
}
