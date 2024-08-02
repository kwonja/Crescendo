package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FeedCommentNotFoundException extends CustomException {
    public FeedCommentNotFoundException() {
        super("존재하지 않는 피드 댓글입니다.", HttpStatus.NOT_FOUND);
    }
}
