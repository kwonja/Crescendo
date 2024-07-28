package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AuthenticationRequiredException extends CustomException {
    public AuthenticationRequiredException() {
        super("JWT 인증 오류", HttpStatus.UNAUTHORIZED);
    }
}
