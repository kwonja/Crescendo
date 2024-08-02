package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FavoriteRankNotFoundException extends CustomException {
    public FavoriteRankNotFoundException() {
        super("존재하지 않는 전국 최애 자랑입니다.", HttpStatus.NOT_FOUND);
    }
}
