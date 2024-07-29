package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.user.dto.request.ProfileUpdateRequest;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;

    @Override
    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String profilePath = user.getProfilePath();

        fileSaveHelper.deleteUserProfile(profilePath);
        String savePath = fileSaveHelper.saveUserProfile(request.getProfileImage());
    }
}
