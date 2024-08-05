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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallenge extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dance_challenge_id")
    private Long danceChallengeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String videoPath;

    @Column(updatable = false)
    private LocalDateTime endAt;

    @Builder
    public DanceChallenge(User user, String title, String videoPath, LocalDateTime endAt) {
        this.user = user;
        this.title = title;
        this.videoPath = videoPath;
        this.endAt = endAt;
    }
}