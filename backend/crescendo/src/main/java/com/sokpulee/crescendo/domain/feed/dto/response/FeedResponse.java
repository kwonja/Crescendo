package com.sokpulee.crescendo.domain.feed.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FeedResponse {
    private Long feedId;
    private Long userId;
    private String profilePath;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private Integer likeCnt;
    private Boolean isLike;
    private List<String> imagePaths; // 리스트 형태
    private String content;
    private Integer commentCnt;
    private List<String> tags; // 리스트 형태

    public FeedResponse(Long feedId, Long userId, String profilePath, String nickname,
                        LocalDateTime createdAt, LocalDateTime lastModified, Integer likeCnt,
                        Boolean isLike, List<String> imagePaths, String content, Integer commentCnt,
                        List<String> tags) {
        this.feedId = feedId;
        this.userId = userId;
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.imagePaths = imagePaths;
        this.content = content;
        this.commentCnt = commentCnt;
        this.tags = tags;
    }
}
