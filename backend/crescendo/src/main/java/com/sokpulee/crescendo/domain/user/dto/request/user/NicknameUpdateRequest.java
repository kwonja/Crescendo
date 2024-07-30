package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameUpdateRequest {

    @NotBlank(message = "닉네임은 필수값입니다.")
    private final String nickname;

    public NicknameUpdateRequest(final String nickname) {
        this.nickname = nickname;
    }
}
