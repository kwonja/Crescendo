package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);
}
