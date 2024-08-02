package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateFavoriteRankException extends CustomException {

    public DuplicateFavoriteRankException() {
        super("한달에 하나의 최애 자랑만 가능합니다.", HttpStatus.CONFLICT);
    }
}
