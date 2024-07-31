package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LoginFailException extends CustomException {
    public LoginFailException() {
        super("로그인 실패", HttpStatus.UNAUTHORIZED);
    }
}
