package com.sokpulee.crescendo.domain.fanart.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FavoriteFanArtResponse {
    private Long fanArtId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    private int likeCnt;

    private boolean isLike;

    private List<String> fanArtImagePathList;

    private String content;

    private int commentCnt;

    public FavoriteFanArtResponse(Long fanArtId,Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime lastModified, int likeCnt, boolean isLike, List<String> fanArtImagePathList, String content, int commentCnt) {
        this.fanArtId = fanArtId;
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
    }
}
