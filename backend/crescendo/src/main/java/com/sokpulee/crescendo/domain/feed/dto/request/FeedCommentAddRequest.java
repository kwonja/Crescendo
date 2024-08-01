package com.sokpulee.crescendo.domain.feed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FeedCommentAddRequest {
    private String content;

    public FeedCommentAddRequest(String content) {
        this.content = content;
    }
}
