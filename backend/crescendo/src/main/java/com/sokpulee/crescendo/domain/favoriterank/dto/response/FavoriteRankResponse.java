package com.sokpulee.crescendo.domain.favoriterank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FavoriteRankResponse {

    private Long writerId;
    private String writerNickname;
    private Long favoriteRankId;
    private String favoriteIdolImagePath;
    private Long likeCnt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isLike;
    private LocalDateTime createdAt;

    public FavoriteRankResponse(Long writerId, String writerNickname, Long favoriteRankId, String favoriteIdolImagePath, Long likeCnt, Boolean isLike, LocalDateTime createdAt) {
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.favoriteRankId = favoriteRankId;
        this.favoriteIdolImagePath = favoriteIdolImagePath;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.createdAt = createdAt;
    }

    public FavoriteRankResponse(Long writerId, String writerNickname, Long favoriteRankId, String favoriteIdolImagePath, Long likeCnt, LocalDateTime createdAt) {
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.favoriteRankId = favoriteRankId;
        this.favoriteIdolImagePath = favoriteIdolImagePath;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
    }
}

