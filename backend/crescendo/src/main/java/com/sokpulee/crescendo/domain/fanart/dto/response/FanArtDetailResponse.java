package com.sokpulee.crescendo.domain.fanart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FanArtDetailResponse {

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private List<String> fanArtImagePathList;

    private String content;

    private int commentCnt;

    private String title;

    @Builder
    public FanArtDetailResponse(Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime lastModified, int likeCnt, boolean isLike, List<String> fanArtImagePathList, String content, int commentCnt, String title) {
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.fanArtImagePathList = fanArtImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.title = title;
    }
}
