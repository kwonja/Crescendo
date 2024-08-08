package com.sokpulee.crescendo.domain.goods.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoodsReplyResponse {

    private Long writerId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private String content;

    public GoodsReplyResponse(Long writerId, String profileImagePath, String nickname, int likeCnt, boolean isLike, String content) {
        this.writerId = writerId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.content = content;
    }
}
