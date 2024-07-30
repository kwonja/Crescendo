package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.auth.*;
import com.sokpulee.crescendo.domain.user.dto.response.auth.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.service.auth.AuthService;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        authService.signUp(signUpRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/email/random-key")
    @Operation(summary = "이메일 인증키 발송", description = "이메일 인증키 발송 API")
    public ResponseEntity<?> emailRandomKey(@Valid @RequestBody EmailRandomKeyRequest emailRandomKeyRequest) {

        EmailRandomKeyResponse response = authService.createEmailRandomKey(emailRandomKeyRequest);

        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/email/validation")
    @Operation(summary = "이메일 인증키 인증", description = "이메일 인증키 인증 API")
    public ResponseEntity<?> emailValidate(@Valid @RequestBody EmailValidationRequest emailValidationRequest) {

        authService.emailValidate(emailValidationRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        Long userId = authService.login(loginRequest);

        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        authService.saveRefreshToken(userId, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Refresh-Token", refreshToken);

        return ResponseEntity.status(OK).headers(headers).build();
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 업데이트 - 비밀번호 찾기", description = "비밀번호 업데이트 - 비밀번호 찾기 API")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        authService.updatePassword(updatePasswordRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

}
