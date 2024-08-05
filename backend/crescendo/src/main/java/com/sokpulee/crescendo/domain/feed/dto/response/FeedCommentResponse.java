package com.sokpulee.crescendo.domain.feed.dto.response;

import lombok.Getter;

@Getter
public class FeedCommentResponse {

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    private boolean isLike;

    private String content;

    private int replyCnt;

    public FeedCommentResponse(Long userId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content, int replyCnt) {
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
        this.replyCnt = replyCnt;
    }
}
