package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DMGroupNotFoundException extends CustomException {
    public DMGroupNotFoundException() {
        super("존재하지 않는 DM 그룹입니다.", HttpStatus.NOT_FOUND);
    }
}
