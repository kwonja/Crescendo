package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmailConflictException extends CustomException {

    public EmailConflictException() {
        super("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
    }
}
