package com.sokpulee.crescendo.domain.fanart.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FanArtSearchCondition {

    private String title;

    private String nickname;

    private String content;

    @Builder
    public FanArtSearchCondition(String title, String nickname, String content) {
        this.title = title;
        this.nickname = nickname;
        this.content = content;
    }
}
