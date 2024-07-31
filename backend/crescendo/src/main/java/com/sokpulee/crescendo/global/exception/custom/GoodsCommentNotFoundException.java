package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class GoodsCommentNotFoundException extends CustomException {
    public GoodsCommentNotFoundException() {
        super("존재하지 않는 굿즈 댓글입니다.", HttpStatus.NOT_FOUND);
    }
}
