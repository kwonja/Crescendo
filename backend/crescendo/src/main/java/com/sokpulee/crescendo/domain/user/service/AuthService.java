package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.dto.response.EmailRandomKeyResponse;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);

    EmailRandomKeyResponse createEmailRandomKey(EmailRandomKeyRequest emailRandomKeyRequest);
}
