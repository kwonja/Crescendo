package com.sokpulee.crescendo.global.exception.custom;

import com.sokpulee.crescendo.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DanceChallengeJoinConflictException extends CustomException {

    public DanceChallengeJoinConflictException() {
        super("이미 참여한 댄스 챌린지 입니다.", HttpStatus.CONFLICT);
    }
}
