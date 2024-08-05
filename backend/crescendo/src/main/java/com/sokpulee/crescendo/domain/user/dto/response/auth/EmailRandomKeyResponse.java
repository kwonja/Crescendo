package com.sokpulee.crescendo.domain.user.dto.response.auth;

import lombok.Getter;

@Getter
public class EmailRandomKeyResponse {

    private final Long emailAuthId;

    public EmailRandomKeyResponse(Long emailAuthId) {
        this.emailAuthId = emailAuthId;
    }
}
