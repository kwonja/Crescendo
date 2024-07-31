package com.sokpulee.crescendo.domain.fanart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class FanArtCommentAddRequest {
    private String content;

    public FanArtCommentAddRequest(String content) {
        this.content = content;
    }
}
