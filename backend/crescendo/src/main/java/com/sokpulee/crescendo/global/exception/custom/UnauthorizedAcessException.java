package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedAcessException extends CustomException {
    public UnauthorizedAcessException() {
        super("해당 데이터에 접근할 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
    }
}
