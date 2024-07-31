package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NicknameConflictException extends CustomException {
    public NicknameConflictException() {
        super("이미 존재하는 닉네임 입니다.", HttpStatus.CONFLICT);
    }
}
