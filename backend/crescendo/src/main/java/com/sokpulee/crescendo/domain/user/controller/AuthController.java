package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.dto.response.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email/random-key")
    public ResponseEntity<?> emailRandomKey(@Valid @RequestBody EmailRandomKeyRequest emailRandomKeyRequest) {

        EmailRandomKeyResponse response = authService.createEmailRandomKey(emailRandomKeyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
