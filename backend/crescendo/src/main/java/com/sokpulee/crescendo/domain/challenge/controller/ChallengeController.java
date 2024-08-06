package com.sokpulee.crescendo.domain.challenge.controller;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import com.sokpulee.crescendo.domain.challenge.service.ChallengeService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
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

    @PostMapping(value = "/{challenge-id}/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "챌린지 참여", description = "챌린지 참여 API")
    public ResponseEntity<?> joinChallenge(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("challenge-id") Long challengeId,
            @Valid @ModelAttribute JoinDanceChallengeRequest joinDanceChallengeRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        challengeService.joinChallenge(loggedInUserId, challengeId, joinDanceChallengeRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping(value = "/join/{challenge-join-id}")
    @Operation(summary = "챌린지 참여 좋아요", description = "챌린지 참여 좋아요 API")
    public ResponseEntity<?> likeChallengeJoin(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("challenge-join-id") Long challengeJoinId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        challengeService.likeChallengeJoin(loggedInUserId, challengeJoinId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/{challenge-id}")
    @Operation(summary = "챌린지 삭제", description = "챌린지 삭제 API")
    public ResponseEntity<?> deleteChallenge(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("challenge-id") Long challengeId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        challengeService.deleteChallenge(loggedInUserId, challengeId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/join/{challenge-join-id}")
    @Operation(summary = "챌린지 참여 삭제", description = "챌린지 참여 삭제 API")
    public ResponseEntity<?> deleteChallengeJoin(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("challenge-join-id") Long challengeJoinId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        challengeService.deleteChallengeJoin(loggedInUserId, challengeJoinId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping
    @Operation(summary = "챌린지 조회", description = "챌린지 조회 API")
    public Page<GetDanceChallengeResponse> getChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ){

        Pageable pageable = PageRequest.of(page, size);

        return challengeService.getChallenges(title, sortBy, pageable);
    }
}
