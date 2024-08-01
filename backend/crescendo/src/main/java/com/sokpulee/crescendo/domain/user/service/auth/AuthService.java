package com.sokpulee.crescendo.domain.user.service.auth;

import com.sokpulee.crescendo.domain.user.dto.request.auth.*;
import com.sokpulee.crescendo.domain.user.dto.response.auth.EmailRandomKeyResponse;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);

    EmailRandomKeyResponse createEmailRandomKey(EmailRandomKeyRequest emailRandomKeyRequest);

    Long login(LoginRequest loginRequest);

    void saveRefreshToken(Long userId, String refreshToken);

    void emailValidate(EmailValidationRequest emailValidationRequest);

    void updatePassword(UpdatePasswordRequest updatePasswordRequest);

    boolean isRefreshTokenValid(Long userId, String refreshToken);

    void deleteRefreshToken(Long userId);
}
