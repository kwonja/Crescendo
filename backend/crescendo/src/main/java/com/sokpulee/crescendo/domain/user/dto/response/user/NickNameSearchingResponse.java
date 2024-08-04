package com.sokpulee.crescendo.domain.user.dto.response.user;

import lombok.Getter;

@Getter
public class NickNameSearchingResponse {

    private Long userId;
    private String userProfilePath;
    private String nickname;

    public NickNameSearchingResponse(Long userId, String userProfilePath, String nickname) {
        this.userId = userId;
        this.userProfilePath = userProfilePath;
        this.nickname = nickname;
    }
}
