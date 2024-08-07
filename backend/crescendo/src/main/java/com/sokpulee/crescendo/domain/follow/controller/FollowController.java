package com.sokpulee.crescendo.domain.follow.controller;

import com.sokpulee.crescendo.domain.follow.dto.request.FollowRequest;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowerListResponse;
import com.sokpulee.crescendo.domain.follow.dto.response.FollowingListResponse;
import com.sokpulee.crescendo.domain.follow.service.FollowService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
@Tag(name = "Follow", description = "팔로우 관련 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    @Operation(summary = "팔로우", description = "팔로우 API")
    public ResponseEntity<?> follow(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestBody FollowRequest followRequest
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        followService.follow(loggedInUserId, followRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/user/{user-id}/following")
    @Operation(summary = "팔로잉 조회", description = "팔로잉 조회 API")
    public ResponseEntity<?> getFollowingList(@PathVariable("user-id") Long userId){

        FollowingListResponse response = followService.getFollwingList(userId);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/user/{user-id}/follower")
    @Operation(summary = "팔로워 조회", description = "팔로워 조회 API")
    public ResponseEntity<?> getFollowerList(@PathVariable("user-id") Long userId) {
        FollowerListResponse response = followService.getFollowerList(userId);
        return ResponseEntity.status(OK).body(response);
    }
}
