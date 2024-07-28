package com.sokpulee.crescendo.domain.challange.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallenge extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}