package com.sokpulee.crescendo.domain.follow.dto.response;

import com.sokpulee.crescendo.domain.follow.dto.UserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowingListResponse {

    List<UserDto> followingList;

    public FollowingListResponse(List<UserDto> followingList) {
        this.followingList = followingList;
    }
}
