package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DanceChallengeJoinNotFoundException extends CustomException {

    public DanceChallengeJoinNotFoundException() {
        super("존재하지 않는 댄스 챌린지 참여 입니다.", HttpStatus.NOT_FOUND);
    }
}
