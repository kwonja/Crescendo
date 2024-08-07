package com.sokpulee.crescendo.domain.fanart.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FanArtResponse {

    private Long fanArtId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    private boolean isLike;

    private List<String> fanArtImagePathList;

    private String content;

    private int commentCnt;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    public FanArtResponse(Long fanArtId, Long userId, String profileImagePath, String nickname, int likeCnt, boolean isLike, List<String> fanArtImagePathList, String content, int commentCnt, LocalDateTime createdAt, LocalDateTime lastModified) {
        this.fanArtId = fanArtId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.fanArtImagePathList = fanArtImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}
