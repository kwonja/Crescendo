package com.sokpulee.crescendo.domain.goods.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
public class GoodsResponse {

    private Long goodsId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private List<String> goodsImagePathList;

    private String content;

    private int commentCnt;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    public GoodsResponse(Long goodsId, Long userId, String profileImagePath, String nickname, int likeCnt, boolean isLike, List<String> goodsImagePathList, String content, int commentCnt, LocalDateTime createdAt, LocalDateTime lastModified) {
        this.goodsId = goodsId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.goodsImagePathList = goodsImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}
