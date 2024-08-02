package com.sokpulee.crescendo.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FeedResponse {
    private Long feedId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer likeCnt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isLike;

    private List<String> feedImagePathList;

    private String content;

    private Integer commentCnt;

    private List<String> tagList;

    public FeedResponse(Long feedId, Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt, Integer likeCnt, String content, Integer commentCnt) {
        this.feedId = feedId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCnt = likeCnt;
        this.content = content;
        this.commentCnt = commentCnt;
    }


    public FeedResponse(Long feedId, Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt, Integer likeCnt, Boolean isLike, List<String> feedImagePathList, String content, Integer commentCnt, List<String> tagList) {
        this.feedId = feedId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.feedImagePathList = feedImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.tagList = tagList;
    }
}
