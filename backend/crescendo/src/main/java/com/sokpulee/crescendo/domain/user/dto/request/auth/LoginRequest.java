package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "이메일은 필수값 입니다.")
    private String email;

    @NotNull(message = "패스워드는 필수값 입니다.")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
