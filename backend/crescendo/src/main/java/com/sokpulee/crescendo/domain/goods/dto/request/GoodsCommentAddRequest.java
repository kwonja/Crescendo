package com.sokpulee.crescendo.domain.goods.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class GoodsCommentAddRequest {
    private String content;

    public GoodsCommentAddRequest(String content) {
        this.content = content;
    }
}
