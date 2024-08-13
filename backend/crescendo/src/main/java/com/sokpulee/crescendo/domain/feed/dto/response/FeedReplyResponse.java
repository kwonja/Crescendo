package com.sokpulee.crescendo.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedReplyResponse {
    private Long writerId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    public FeedReplyResponse(Long writerId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content, LocalDateTime createdAt, LocalDateTime lastModified) {
        this.writerId = writerId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}
