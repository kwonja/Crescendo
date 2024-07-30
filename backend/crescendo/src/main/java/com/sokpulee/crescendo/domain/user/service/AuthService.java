package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.EmailValidationRequest;
import com.sokpulee.crescendo.domain.user.dto.request.LoginRequest;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.dto.response.EmailRandomKeyResponse;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);

    EmailRandomKeyResponse createEmailRandomKey(EmailRandomKeyRequest emailRandomKeyRequest);

    Long login(LoginRequest loginRequest);

    void saveRefreshToken(Long userId, String refreshToken);

    void emailValidate(EmailValidationRequest emailValidationRequest);
}
