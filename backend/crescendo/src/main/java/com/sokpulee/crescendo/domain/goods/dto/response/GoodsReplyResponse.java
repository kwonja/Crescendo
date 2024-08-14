package com.sokpulee.crescendo.domain.goods.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GoodsReplyResponse {

    private Long writerId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    private Long goodsCommentId;

    public GoodsReplyResponse(Long writerId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content, LocalDateTime createdAt, LocalDateTime lastModified, Long goodsCommentId) {
        this.writerId = writerId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.goodsCommentId = goodsCommentId;
    }
}
