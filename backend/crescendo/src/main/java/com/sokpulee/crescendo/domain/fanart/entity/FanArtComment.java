package com.sokpulee.crescendo.domain.fanart.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    private int likeCnt;

    private int replyCnt;

    @Builder
    public FanArtComment(FanArt fanArt, FanArtComment parentFanArtComment, User user, String content, int likeCnt, int replyCnt) {
        this.fanArt = fanArt;
        this.parentFanArtComment = parentFanArtComment;
        this.user = user;
        this.content = content;
        this.likeCnt = likeCnt;
        this.replyCnt = replyCnt;
    }

    public void changeComment(String content){ this.content = content; }

    public void minusLikeCnt(){ likeCnt--; }

    public void plusLikeCnt(){ likeCnt++; }

    public void plusReplyCnt(){ replyCnt++; }

    public void minusReplyCnt(){ replyCnt--; }
}