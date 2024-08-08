package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ChallengeNotFoundException extends CustomException {

    public ChallengeNotFoundException() {
        super("챌린지가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
