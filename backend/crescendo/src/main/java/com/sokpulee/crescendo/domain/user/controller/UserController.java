package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.ProfileUpdateRequest;
import com.sokpulee.crescendo.domain.user.service.UserService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @PatchMapping("/mypage/profile")
    @Operation(summary = "프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<?> updateProfile(
            @AuthPrincipal Long userId,
            @ModelAttribute ProfileUpdateRequest request) {

        userService.updateProfile(userId, request);

        return ResponseEntity.noContent().build();
    }
}
