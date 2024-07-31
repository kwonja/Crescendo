package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NickNameExistsRequest {

    @NotBlank(message = "닉네임은 필수값 입니다.")
    private String nickname;

    public NickNameExistsRequest(String nickname) {
        this.nickname = nickname;
    }
}
