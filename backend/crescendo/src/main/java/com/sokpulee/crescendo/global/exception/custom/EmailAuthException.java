package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmailAuthException extends CustomException {

    public EmailAuthException() {
        super("이메일 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED);
    }
}
