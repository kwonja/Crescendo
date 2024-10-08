package com.sokpulee.crescendo.domain.challenge.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetDanceChallengeResponse {

    private Long challengeId;
    private String title;
    private String challengeVideoPath;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;
    private Long userId;
    private String nickname;
    private String profilePath;
    private int participants;

    public GetDanceChallengeResponse(Long challengeId, String title, String challengeVideoPath, LocalDateTime createdAt, LocalDateTime endAt, Long userId, String nickname, String profilePath, int participants) {
        this.challengeId = challengeId;
        this.title = title;
        this.challengeVideoPath = challengeVideoPath;
        this.createdAt = createdAt;
        this.endAt = endAt;
        this.userId = userId;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.participants = participants;
    }
}
