package com.sokpulee.crescendo.domain.follow.service;

import com.sokpulee.crescendo.domain.alarm.service.AlarmService;
import com.sokpulee.crescendo.domain.follow.dto.UserDto;
import com.sokpulee.crescendo.domain.follow.dto.request.FollowRequest;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowerListResponse;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowingListResponse;
import com.sokpulee.crescendo.domain.follow.entity.Follow;
import com.sokpulee.crescendo.domain.follow.repository.FollowRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Override
    public void follow(Long loggedInUserId, FollowRequest followRequest) {

        User loggedInUser = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        User followUser = userRepository.findById(followRequest.getUserIdToFollow())
                .orElseThrow(UserNotFoundException::new);

        Follow follow = Follow.builder()
                .following(loggedInUser)
                .follower(followUser)
                .build();

        followRepository.save(follow);

        alarmService.followAlarm(loggedInUser.getId(), followUser.getId(), loggedInUser.getId());
    }

    @Override
    public FollowingListResponse getFollwingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Follow> followList = followRepository.findByFollowing(user);
        List<UserDto> followingDtoList = followList.stream()
                .map(follow -> new UserDto(follow.getFollower().getId(), follow.getFollower().getNickname(), follow.getFollower().getProfilePath()))
                .collect(Collectors.toList());

        return new FollowingListResponse(followingDtoList);
    }

    @Override
    public FollowerListResponse getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Follow> followList = followRepository.findByFollower(user);
        List<UserDto> followerDtoList = followList.stream()
                .map(follow -> new UserDto(follow.getFollowing().getId(), follow.getFollowing().getNickname(), follow.getFollowing().getProfilePath()))
                .collect(Collectors.toList());

        return new FollowerListResponse(followerDtoList);
    }
}
