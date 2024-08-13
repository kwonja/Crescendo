package com.sokpulee.crescendo.domain.fanart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FanArtCommentResponse {

    private Long fanArtCommentId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private String content;

    private int replyCnt;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    public FanArtCommentResponse(Long fanArtCommentId, Long userId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content, int replyCnt, LocalDateTime createdAt, LocalDateTime lastModified) {
        this.fanArtCommentId = fanArtCommentId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
        this.replyCnt = replyCnt;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}
