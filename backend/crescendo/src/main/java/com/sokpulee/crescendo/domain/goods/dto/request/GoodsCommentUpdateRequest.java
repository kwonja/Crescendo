package com.sokpulee.crescendo.domain.goods.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GoodsCommentUpdateRequest {

    @NotBlank
    private String content;

    public GoodsCommentUpdateRequest(String content) {
        this.content = content;
    }
}
