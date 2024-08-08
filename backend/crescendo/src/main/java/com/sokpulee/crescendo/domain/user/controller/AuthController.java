package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.auth.*;
import com.sokpulee.crescendo.domain.user.dto.response.auth.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.dto.response.auth.LoginResponse;
import com.sokpulee.crescendo.domain.user.service.auth.AuthService;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {

    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @GetMapping("/set-cookie")
    @Operation(summary = "쿠키 테스트용", description = "쿠키 테스트 API")
    public String setCookie(HttpServletResponse response) {
        // 쿠키 생성
        Cookie cookie = new Cookie("testCookie", "testValue");
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효

        // 응답에 쿠키 추가
        response.addCookie(cookie);

        return "쿠키가 설정되었습니다.";
    }

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

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpireTime)
                .sameSite("None")
                .build();

        return ResponseEntity.status(OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(new LoginResponse(userId));
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 업데이트 - 비밀번호 찾기", description = "비밀번호 업데이트 - 비밀번호 찾기 API")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        authService.updatePassword(updatePasswordRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "AccessToken 재발급", description = "AccessToken 재발급 API")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null || !jwtUtil.checkRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        Long userId = jwtUtil.getUserIdByRefreshToken(refreshToken);
        if (userId == null || !authService.isRefreshTokenValid(userId, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.createAccessToken(userId);

        return ResponseEntity.status(OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtUtil.checkRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        Long userId = jwtUtil.getUserIdByRefreshToken(refreshToken);
        if (userId != null) {
            authService.deleteRefreshToken(userId);
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 쿠키 삭제
                .build();

        return ResponseEntity.status(OK)
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}
