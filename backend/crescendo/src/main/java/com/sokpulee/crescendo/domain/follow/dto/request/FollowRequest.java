package com.sokpulee.crescendo.domain.follow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowRequest {

    @NotNull(message = "유저 아이디는 필수값입니다.")
    private Long userIdToFollow;

    public FollowRequest(Long userIdToFollow) {
        this.userIdToFollow = userIdToFollow;
    }
}
