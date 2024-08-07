package com.sokpulee.crescendo.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MyFeedResponse {

    private Long feedId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private List<String> feedImagePathList;

    private String content;

    private int commentCnt;

    private List<String> tagList;

    public MyFeedResponse(Long feedId,Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime lastModified, int likeCnt, boolean isLike, List<String> feedImagePathList, String content, int commentCnt, List<String> tagList) {
        this.feedId = feedId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.feedImagePathList = feedImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.tagList = tagList;
    }
}
