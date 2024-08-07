package com.sokpulee.crescendo.domain.goods.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FavoriteGoodsResponse {
    private Long goodsId;

    private Long userId;

    private String profileImagePath;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

    private int likeCnt;

    @JsonProperty("isLike")
    private boolean isLike;

    private List<String> goodsImagePathList;

    private String content;

    private int commentCnt;

    private String title;

    public FavoriteGoodsResponse(Long goodsId, Long userId, String profileImagePath, String nickname, LocalDateTime createdAt, LocalDateTime lastModified, int likeCnt, boolean isLike, List<String> goodsImagePathList, String content, int commentCnt, String title) {
        this.goodsId = goodsId;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.goodsImagePathList = goodsImagePathList;
        this.content = content;
        this.commentCnt = commentCnt;
        this.title = title;
    }
}
