package com.sokpulee.crescendo.domain.challenge.dto.response;

import lombok.Getter;

@Getter
public class ChallengeVideoResponse {

    private String challengeVideoPath;

    public ChallengeVideoResponse(String challengeVideoPath) {
        this.challengeVideoPath = challengeVideoPath;
    }
}
