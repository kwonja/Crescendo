package com.sokpulee.crescendo.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotNull(message = "이메일은 필수값 입니다.")
    private final String email;

    @NotNull(message = "패스워드는 필수값 입니다.")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
