package com.sokpulee.crescendo.domain.user.service.user;

import com.sokpulee.crescendo.domain.follow.repository.FollowRepository;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.idol.IdolRepository;
import com.sokpulee.crescendo.domain.user.dto.request.user.*;
import com.sokpulee.crescendo.domain.user.dto.response.user.NickNameSearchingResponse;
import com.sokpulee.crescendo.domain.user.dto.response.user.UserInfoResponse;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.encrypt.EnctyptHelper;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;
    private final FollowRepository followRepository;
    private final EnctyptHelper enctyptHelper;
    private final IdolRepository idolRepository;

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
    public void nicknameExists(String nickname) {

        if(userRepository.findByNickname(nickname).isPresent()) {
            throw new NicknameConflictException();
        }
    }

    @Override
    public void emailExists(EmailExistsRequest emailExistsRequest) {

        if(userRepository.findByEmail(emailExistsRequest.getEmail()).isPresent()) {
            throw new EmailConflictException();
        }
    }

    @Override
    public UserInfoResponse getUserById(Long loggedInUserId, Long findUserId) {
        User user = userRepository.findById(findUserId)
                .orElseThrow(UserNotFoundException::new);

        UserInfoResponse.UserInfoResponseBuilder userInfo = UserInfoResponse
                .builder()
                .profilePath(user.getProfilePath())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .followerNum(followRepository.countByFollower(user))
                .followingNum(followRepository.countByFollowing(user));

        if(loggedInUserId != null) {
            User loggedInUser = userRepository.findById(loggedInUserId).orElseThrow(UserNotFoundException::new);
            userInfo
                    .isFollowing(followRepository.existsByFollowingAndFollower(loggedInUser, user));
        }

        return userInfo.build();
    }

    @Override
    public void deleteUserById(Long loggedInUserId) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }

    @Override
    public void updateNickname(Long loggedInUserId, NicknameUpdateRequest nicknameUpdateRequest) {
        nicknameExists(nicknameUpdateRequest.getNickname());

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);
        user.updateNickname(nicknameUpdateRequest.getNickname());
    }

    @Override
    public void updateIntroduction(Long loggedInUserId, IntroductionUpdateRequest introductionUpdateRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);
        user.updateIntroduction(introductionUpdateRequest.getIntroduction());

    }

    @Override
    public void updatePassword(Long loggedInUserId, PasswordUpdateMyPageRequest passwordUpdateMyPageRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        if(!enctyptHelper.isMatch(passwordUpdateMyPageRequest.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectCurrentPasswordException();
        }
        else {
            user.updatePassword(enctyptHelper.encrypt(passwordUpdateMyPageRequest.getNewPassword()));
        }
    }

    @Override
    public Page<NickNameSearchingResponse> searchUsersByNickname(String nickname, Pageable pageable) {
        Page<User> users = userRepository.findByNickname(nickname, pageable);
        List<NickNameSearchingResponse> results = users.getContent().stream()
                .map(user -> new NickNameSearchingResponse(user.getId(), user.getProfilePath(), user.getNickname()))
                .toList();

        return new PageImpl<>(results, pageable, users.getTotalElements());
    }

    @Override
    public void updateFavoriteIdol(Long loggedInUserId, UpdateFavoriteIdolRequest updateFavoriteIdolRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Idol idol = idolRepository.findById(updateFavoriteIdolRequest.getIdolId())
                .orElseThrow(IdolNotFoundException::new);

        user.updateIdol(idol);
    }
}
