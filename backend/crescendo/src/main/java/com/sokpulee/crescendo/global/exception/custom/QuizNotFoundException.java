package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends CustomException {

    public QuizNotFoundException() {
        super("존재하지 않는 퀴즈입니다.", HttpStatus.NOT_FOUND);
    }
}
