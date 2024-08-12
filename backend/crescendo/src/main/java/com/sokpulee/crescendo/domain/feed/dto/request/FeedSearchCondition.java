package com.sokpulee.crescendo.domain.feed.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedSearchCondition {

    private String nickname;

    private String content;

    private Boolean sortByFollowed;

    private Boolean sortByLiked;

    @Builder
    public FeedSearchCondition(String nickname, String content, Boolean sortByFollowed, Boolean sortByLiked) {
        this.nickname = nickname;
        this.content = content;
        this.sortByFollowed = sortByFollowed;
        this.sortByLiked = sortByLiked;
    }
}
