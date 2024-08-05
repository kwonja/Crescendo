package com.sokpulee.crescendo.domain.challenge.controller;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.service.ChallengeService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challange")
@Tag(name = "Challenge", description = "챌린지 관련 API")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "챌린지 생성", description = "챌린지 생성 API")
    public ResponseEntity<?> createChallange(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @Valid @ModelAttribute CreateDanceChallengeRequest createDanceChallengeRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        challengeService.createChallenge(loggedInUserId, createDanceChallengeRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
