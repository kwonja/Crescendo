package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FeedNotFoundException extends CustomException {
    public FeedNotFoundException() {
        super("존재하지 않는 피드입니다.", HttpStatus.NOT_FOUND);
    }
}
