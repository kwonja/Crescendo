package com.sokpulee.crescendo.domain.challenge.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallengeJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dance_challenge_join_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dance_challenge_id")
    private DanceChallenge danceChallenge;

    @Column(length = 500)
    private String videoPath;

    private Integer score;

    @Builder
    public DanceChallengeJoin(User user, DanceChallenge danceChallenge, String videoPath) {
        this.user = user;
        this.danceChallenge = danceChallenge;
        this.videoPath = videoPath;
    }
}