package com.sokpulee.crescendo.domain.user.dto.response.auth;

import lombok.Getter;

@Getter
public class LoginResponse {

    private Long userId;

    public LoginResponse(Long userId) {
        this.userId = userId;
    }
}
