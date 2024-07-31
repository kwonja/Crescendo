package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FanArtNotFoundException extends CustomException {
    public FanArtNotFoundException() {
        super("존재하지 않는 팬아트입니다.", HttpStatus.NOT_FOUND);
    }
}
