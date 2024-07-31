package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FileDeleteFailException extends CustomException {
    public FileDeleteFailException() {
        super("파일 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
