package com.sokpulee.crescendo.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private LocalDateTime lastModified;

    private Integer likeCnt;

    @JsonProperty("isLike")
    private Boolean isLike;

    private List<String> feedImagePathList; // 리스트 형태

    private String content;

    private Integer commentCnt;

    private List<String> tags; // 리스트 형태

    public FeedResponse(Long feedId, Long userId, String profileImagePath, String nickname,
                        LocalDateTime createdAt, LocalDateTime lastModified, Integer likeCnt,
                        Boolean isLike, List<String> feedImagePathList, String content, Integer commentCnt,
                        List<String> tags) {
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
        this.tags = tags;
    }
}
