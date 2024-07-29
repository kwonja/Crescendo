package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.user.dto.request.ProfileUpdateRequest;

public interface UserService {
    void updateProfile(Long userId, ProfileUpdateRequest request);
}
