package com.sokpulee.crescendo.domain.user.service.user;

import com.sokpulee.crescendo.domain.user.dto.request.user.EmailExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.NickNameExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.ProfileUpdateRequest;

public interface UserService {
    void updateProfile(Long userId, ProfileUpdateRequest request);

    void nicknameExists(NickNameExistsRequest nickNameExistsRequest);

    void emailExists(EmailExistsRequest emailExistsRequest);
}
