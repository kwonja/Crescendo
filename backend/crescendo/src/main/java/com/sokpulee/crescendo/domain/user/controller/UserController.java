package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.user.EmailExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.NickNameExistsRequest;
import com.sokpulee.crescendo.domain.user.dto.request.user.ProfileUpdateRequest;
import com.sokpulee.crescendo.domain.user.dto.response.user.UserInfoResponse;
import com.sokpulee.crescendo.domain.user.service.user.UserService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
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
    private final JWTUtil jwtUtil;

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
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 API")
    public ResponseEntity<?> nicknameExists(@RequestBody NickNameExistsRequest nickNameExistsRequest) {

        userService.nicknameExists(nickNameExistsRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/email/exists")
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크 API")
    public ResponseEntity<?> emailExists(@RequestBody EmailExistsRequest emailExistsRequest) {

        userService.emailExists(emailExistsRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/{user-id}")
    @Operation(summary = "사용자 정보 불러오기", description = "아이디 기반 사용자 정보 불러오기 API")
    public ResponseEntity<?> getUserById(
            @AuthPrincipal Long loggedInUserId,
            @PathVariable("user-id") Long findUserId
    ) {
        UserInfoResponse userInfoResponse = userService.getUserById(loggedInUserId, findUserId);

        return ResponseEntity.status(OK).body(userInfoResponse);
    }
}
