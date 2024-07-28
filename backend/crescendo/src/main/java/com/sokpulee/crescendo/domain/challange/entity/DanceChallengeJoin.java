package com.sokpulee.crescendo.domain.challange.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallengeJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long danceChallengeJoinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dance_challenge_id")
    private DanceChallenge danceChallenge;

    @Column(length = 500)
    private String videoPath;

    private Integer score;
}