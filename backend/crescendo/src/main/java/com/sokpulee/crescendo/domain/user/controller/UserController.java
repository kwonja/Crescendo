package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.user.NickNameExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.ProfileUpdateRequest;
import com.sokpulee.crescendo.domain.user.service.user.UserService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @PatchMapping(value = "/mypage/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<?> updateProfile(
            @AuthPrincipal Long userId,
            @ModelAttribute ProfileUpdateRequest request
    ) {

        userService.updateProfile(userId, request);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/nickname/exists")
    public ResponseEntity<?> nicknameExists(@RequestBody NickNameExistsRequest nickNameExistsRequest) {

        userService.nicknameExists(nickNameExistsRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
