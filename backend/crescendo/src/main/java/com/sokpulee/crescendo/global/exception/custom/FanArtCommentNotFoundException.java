package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FanArtCommentNotFoundException extends CustomException {
    public FanArtCommentNotFoundException() {
        super("존재하지 않는 팬아트 댓글입니다.", HttpStatus.NOT_FOUND);
    }
}
