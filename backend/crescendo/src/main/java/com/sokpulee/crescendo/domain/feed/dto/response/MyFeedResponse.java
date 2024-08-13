package com.sokpulee.crescendo.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
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

    private String idolGroupName;

    private Long idolGroupId;

    public MyFeedResponse(Long feedId,Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime lastModified, int likeCnt, boolean isLike, List<String> feedImagePathList, String content, int commentCnt, List<String> tagList, String idolGroupName, Long idolGroupId) {
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
        this.idolGroupName = idolGroupName;
        this.idolGroupId = idolGroupId;
    }
}
