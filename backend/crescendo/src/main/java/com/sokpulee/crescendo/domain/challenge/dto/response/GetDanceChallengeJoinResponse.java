package com.sokpulee.crescendo.domain.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class GetDanceChallengeJoinResponse {

    private Long challengeJoinId;
    private String challengeVideoPath;
    private Double score;
    private Long userId;
    private String nickname;
    private int likeCnt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isLike;

    public GetDanceChallengeJoinResponse(Long challengeJoinId, String challengeVideoPath, Double score, Long userId, String nickname, int likeCnt, Boolean isLike) {
        this.challengeJoinId = challengeJoinId;
        this.challengeVideoPath = challengeVideoPath;
        this.score = score;
        this.userId = userId;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
    }

    public GetDanceChallengeJoinResponse(Long challengeJoinId, String challengeVideoPath, Double score, Long userId, String nickname, int likeCnt) {
        this.challengeJoinId = challengeJoinId;
        this.challengeVideoPath = challengeVideoPath;
        this.score = score;
        this.userId = userId;
        this.nickname = nickname;
        this.likeCnt = likeCnt;
    }
}
