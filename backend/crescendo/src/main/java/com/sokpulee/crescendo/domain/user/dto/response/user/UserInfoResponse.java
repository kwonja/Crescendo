package com.sokpulee.crescendo.domain.user.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {

    private String profilePath;

    private String nickname;

    private String introduction;

    private Long followingNum;

    private Long followerNum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFollowing;

    private String favoriteImagePath;

    @Builder
    public UserInfoResponse(String profilePath, String nickname, String introduction, Long followingNum, Long followerNum, Boolean isFollowing, String favoriteImagePath) {
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingNum = followingNum;
        this.followerNum = followerNum;
        this.isFollowing = isFollowing;
        this.favoriteImagePath = favoriteImagePath;
    }
}
