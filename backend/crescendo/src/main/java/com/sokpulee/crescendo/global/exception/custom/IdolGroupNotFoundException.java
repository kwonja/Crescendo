package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class IdolGroupNotFoundException extends CustomException {
    public IdolGroupNotFoundException() {
        super("존재하지 않는 아이돌 그룹입니다.", HttpStatus.NOT_FOUND);
    }
}
