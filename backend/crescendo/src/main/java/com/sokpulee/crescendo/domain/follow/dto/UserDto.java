package com.sokpulee.crescendo.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private Long userId;
    private String nickname;
    private String userProfilePath;

    @Builder
    public UserDto(Long userId, String nickname, String userProfilePath) {
        this.userId = userId;
        this.nickname = nickname;
        this.userProfilePath = userProfilePath;
    }
}
