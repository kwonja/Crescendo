package com.sokpulee.crescendo.domain.follow.service;

import com.sokpulee.crescendo.domain.follow.dto.request.FollowRequest;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowerListResponse;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowingListResponse;

public interface FollowService {
    void follow(Long loggedInUserId, FollowRequest followRequest);

    FollowingListResponse getFollwingList(Long userId);

    FollowerListResponse getFollowerList(Long userId);
}
