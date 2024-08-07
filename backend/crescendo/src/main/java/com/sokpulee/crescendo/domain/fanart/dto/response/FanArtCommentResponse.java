package com.sokpulee.crescendo.domain.fanart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

    public FanArtCommentResponse(Long fanArtCommentId, Long userId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content, int replyCnt) {
        this.fanArtCommentId = fanArtCommentId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
        this.replyCnt = replyCnt;
    }
}
