package com.sokpulee.crescendo.domain.challenge.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallengeJoin extends CreatedAtEntity {
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

    private Double score;

    @OneToMany(mappedBy = "danceChallengeJoin",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DanceChallengeJoinLike> danceChallengeJoinLikes = new ArrayList<>();

    @Builder

    public DanceChallengeJoin(User user, DanceChallenge danceChallenge, String videoPath, Double score, List<DanceChallengeJoinLike> danceChallengeJoinLikes) {
        this.user = user;
        this.danceChallenge = danceChallenge;
        this.videoPath = videoPath;
        this.score = score;
        this.danceChallengeJoinLikes = danceChallengeJoinLikes;
    }
}