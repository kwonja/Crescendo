package com.sokpulee.crescendo.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FeedCommentUpdateRequest {

    @NotBlank
    private String content;

    public FeedCommentUpdateRequest(String content) {
        this.content = content;
    }
}
