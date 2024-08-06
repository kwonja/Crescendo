package com.sokpulee.crescendo.domain.challenge.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallenge extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dance_challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String videoPath;

    @Column(updatable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "danceChallenge",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DanceChallengeJoin> danceChallengeJoins = new ArrayList<>();

    @Builder
    public DanceChallenge(User user, String title, String videoPath, LocalDateTime endAt, List<DanceChallengeJoin> danceChallengeJoins) {
        this.user = user;
        this.title = title;
        this.videoPath = videoPath;
        this.endAt = endAt;
        this.danceChallengeJoins = danceChallengeJoins;
    }
}