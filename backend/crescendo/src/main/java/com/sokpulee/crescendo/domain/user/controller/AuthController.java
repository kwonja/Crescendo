package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.EmailValidationRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> emailRandomKey(@Valid @RequestBody EmailRandomKeyRequest emailRandomKeyRequest) {

        EmailRandomKeyResponse response = authService.createEmailRandomKey(emailRandomKeyRequest);

        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/email/validation")
    public ResponseEntity<?> emailValidate(@Valid @RequestBody EmailValidationRequest emailValidationRequest) {

        authService.emailValidate(emailValidationRequest);

        return ResponseEntity.status(NO_CONTENT).build();
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

        return ResponseEntity.status(OK).headers(headers).build();
    }
}
