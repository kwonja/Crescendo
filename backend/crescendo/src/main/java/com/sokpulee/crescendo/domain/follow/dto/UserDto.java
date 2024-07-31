package com.sokpulee.crescendo.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private Long userId;
    private String nickname;
    private String profilePath;

    @Builder
    public UserDto(Long userId, String nickname, String profilePath) {
        this.userId = userId;
        this.nickname = nickname;
        this.profilePath = profilePath;
    }
}
