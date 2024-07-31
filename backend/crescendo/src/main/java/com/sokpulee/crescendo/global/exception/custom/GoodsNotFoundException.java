package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class GoodsNotFoundException extends CustomException {
    public GoodsNotFoundException() {
        super("존재하지 않는 굿즈입니다.", HttpStatus.NOT_FOUND);
    }
}
