package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class IncorrectCurrentPasswordException extends CustomException {

    public IncorrectCurrentPasswordException() {
        super("현재 비밀번호와 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
