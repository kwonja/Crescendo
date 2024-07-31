package com.sokpulee.crescendo.domain.follow.dto.response;

import com.sokpulee.crescendo.domain.follow.dto.UserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowerListResponse {

    private List<UserDto> followerList;

    public FollowerListResponse(List<UserDto> followerList) {
        this.followerList = followerList;
    }
}
