package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.LoginRequest;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.dto.response.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.service.AuthService;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email/random-key")
    public ResponseEntity<?> emailRandomKey(@Valid @RequestBody EmailRandomKeyRequest emailRandomKeyRequest) {

        EmailRandomKeyResponse response = authService.createEmailRandomKey(emailRandomKeyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        Long userId = authService.login(loginRequest);

        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        authService.saveRefreshToken(userId, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Refresh-Token", refreshToken);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }
}
