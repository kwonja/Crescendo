package com.sokpulee.crescendo.domain.user.service.user;

import com.sokpulee.crescendo.domain.user.dto.request.user.*;
import com.sokpulee.crescendo.domain.user.dto.response.user.UserInfoResponse;

public interface UserService {
    void updateProfile(Long userId, ProfileUpdateRequest request);

    void nicknameExists(String nickname);

    void emailExists(EmailExistsRequest emailExistsRequest);

    UserInfoResponse getUserById(Long loggedInUserId, Long findUserId);

    void deleteUserById(Long loggedInUserId);

    void updateNickname(Long loggedInUserId, NicknameUpdateRequest nicknameUpdateRequest);

    void updateIntroduction(Long loggedInUserId, IntroductionUpdateRequest introductionUpdateRequest);

    void updatePassword(Long loggedInUserId, PasswordUpdateMyPageRequest passwordUpdateMyPageRequest);
}
