package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlarmChannelNotFoundException extends CustomException {

    public AlarmChannelNotFoundException() {
        super("존재하지 않는 알람 채널입니다.", HttpStatus.NOT_FOUND);
    }
}
