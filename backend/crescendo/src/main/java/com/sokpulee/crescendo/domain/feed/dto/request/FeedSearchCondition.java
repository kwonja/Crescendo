package com.sokpulee.crescendo.domain.feed.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedSearchCondition {

    private String nickname;

    private String content;

    @Builder
    public FeedSearchCondition(String nickname, String content) {
        this.nickname = nickname;
        this.content = content;
    }
}
