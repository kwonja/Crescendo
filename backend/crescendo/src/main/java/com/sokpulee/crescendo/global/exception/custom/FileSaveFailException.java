package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FileSaveFailException extends CustomException {
    public FileSaveFailException() {
        super("파일 저장 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
