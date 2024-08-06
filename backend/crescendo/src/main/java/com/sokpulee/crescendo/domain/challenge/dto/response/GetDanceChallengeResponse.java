package com.sokpulee.crescendo.domain.challenge.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetDanceChallengeResponse {
    private String title;
    private String challengeVideoPath;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;
    private Long userId;
    private String nickname;
    private int participants;

    public GetDanceChallengeResponse(String title, String challengeVideoPath, LocalDateTime createdAt, LocalDateTime endAt, Long userId, String nickname, int participants) {
        this.title = title;
        this.challengeVideoPath = challengeVideoPath;
        this.createdAt = createdAt;
        this.endAt = endAt;
        this.userId = userId;
        this.nickname = nickname;
        this.participants = participants;
    }
}
