package com.sokpulee.crescendo.domain.fanart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FanArtCommentUpdateRequest {

    @NotBlank
    private String content;

    public FanArtCommentUpdateRequest(String content) {
        this.content = content;
    }
}
