package com.sokpulee.crescendo.domain.fanart.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FanArtComment extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fanArtCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_art_id")
    private FanArt fanArt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_fan_art_comment_id")
    private FanArtComment parentFanArtComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 200)
    private String content;
}