package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlarmNotFoundException extends CustomException {

    public AlarmNotFoundException() {
        super("존재하지 않는 알람입니다.", HttpStatus.NOT_FOUND);
    }
}
