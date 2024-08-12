package com.sokpulee.crescendo.domain.fanart.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FanArtCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fanArtCommentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_art_comment_id")
    private FanArtComment fanArtComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public FanArtCommentLike(FanArtComment fanArtComment, User user) {
        this.fanArtComment = fanArtComment;
        this.user = user;
    }
}