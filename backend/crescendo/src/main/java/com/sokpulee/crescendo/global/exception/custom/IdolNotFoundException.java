package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class IdolNotFoundException extends CustomException {
    public IdolNotFoundException() {
        super("존재하지 않는 아이돌입니다.", HttpStatus.NOT_FOUND);
    }
}
