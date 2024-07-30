package com.sokpulee.crescendo.domain.user.service.user;

import com.sokpulee.crescendo.domain.user.dto.request.user.EmailExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.NickNameExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.ProfileUpdateRequest;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.EmailConflictException;
import com.sokpulee.crescendo.global.exception.custom.NicknameConflictException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;

    @Override
    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String profilePath = user.getProfilePath();

        System.out.println("jdklsjfldksjf" + profilePath);
        fileSaveHelper.deleteFile(profilePath);
        String savePath = fileSaveHelper.saveUserProfile(request.getProfileImage());
        user.changeProfilePath(savePath);
    }

    @Override
    public void nicknameExists(NickNameExistsRequest nickNameExistsRequest) {

        if(userRepository.findByNickname(nickNameExistsRequest.getNickname()).isPresent()) {
            throw new NicknameConflictException();
        }
    }

    @Override
    public void emailExists(EmailExistsRequest emailExistsRequest) {

        if(userRepository.findByEmail(emailExistsRequest.getEmail()).isPresent()) {
            throw new EmailConflictException();
        }
    }
}
