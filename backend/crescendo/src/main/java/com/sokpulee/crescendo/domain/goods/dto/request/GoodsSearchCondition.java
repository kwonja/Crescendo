package com.sokpulee.crescendo.domain.goods.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GoodsSearchCondition {

    private String title;

    private String nickname;

    private String content;

    @Builder
    public GoodsSearchCondition(String title, String nickname, String content) {
        this.title = title;
        this.nickname = nickname;
        this.content = content;
    }
}
