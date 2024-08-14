package com.sokpulee.crescendo.domain.user.controller;

import com.sokpulee.crescendo.domain.follow.dto.UserDto;
import com.sokpulee.crescendo.domain.user.dto.request.user.*;
import com.sokpulee.crescendo.domain.user.dto.response.user.NickNameSearchingResponse;
import com.sokpulee.crescendo.domain.user.dto.response.user.UserInfoResponse;
import com.sokpulee.crescendo.domain.user.service.user.UserService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
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
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/nickname/exists")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 API")
    public ResponseEntity<?> nicknameExists(@Valid @RequestBody NickNameExistsRequest nickNameExistsRequest) {

        userService.nicknameExists(nickNameExistsRequest.getNickname());

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/email/exists")
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크 API")
    public ResponseEntity<?> emailExists(@Valid @RequestBody EmailExistsRequest emailExistsRequest) {

        userService.emailExists(emailExistsRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/{user-id}")
    @Operation(summary = "사용자 정보 불러오기", description = "아이디 기반 사용자 정보 불러오기 API")
    public ResponseEntity<?> getUserById(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("user-id") Long findUserId
    ) {
        UserInfoResponse userInfoResponse = userService.getUserById(loggedInUserId, findUserId);

        return ResponseEntity.status(OK).body(userInfoResponse);
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API")
    public ResponseEntity<?> deleteUserById(@AuthPrincipal Long loggedInUserId) {
        if(loggedInUserId != null) {
            throw new AuthenticationRequiredException();
        }

        userService.deleteUserById(loggedInUserId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping(value = "/mypage/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<?> updateProfile(
            @Parameter(hidden = true) @AuthPrincipal Long userId,
            @Valid @ModelAttribute ProfileUpdateRequest request
    ) {

        if(userId == null) {
            throw new AuthenticationRequiredException();
        }
        userService.updateProfile(userId, request);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/mypage/nickname")
    @Operation(summary = "닉네임 수정", description = "닉네임 수정 API")
    public ResponseEntity<?> updateNickname(
            @AuthPrincipal Long loggedInUserId,
            @Valid @RequestBody NicknameUpdateRequest nicknameUpdateRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        userService.updateNickname(loggedInUserId, nicknameUpdateRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/mypage/introduction")
    @Operation(summary = "자기소개 수정", description = "자기소개 수정 API")
    public ResponseEntity<?> updateIntroduction(
            @AuthPrincipal Long loggedInUserId,
            @Valid @RequestBody IntroductionUpdateRequest introductionUpdateRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        userService.updateIntroduction(loggedInUserId, introductionUpdateRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/mypage/password")
    @Operation(summary = "비밀번호 수정 - 마이페이지", description = "비밀번호 수정 - 마이페이지 API")
    public ResponseEntity<?> updatePassword(
            @AuthPrincipal Long loggedInUserId,
            @Valid @RequestBody PasswordUpdateMyPageRequest passwordUpdateMyPageRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        userService.updatePassword(loggedInUserId, passwordUpdateMyPageRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/search")
    @Operation(summary = "회원 검색(닉네임)", description = "회원 검색(닉네임) API")
    public Page<NickNameSearchingResponse> searchUsersByNickname(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam("nickname") String nickname
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsersByNickname(nickname, pageable);
    }

    @PatchMapping("/favorite-idol")
    @Operation(summary = "최애 아이돌 변경", description = "최애 아이돌 변경 API")
    public ResponseEntity<?> updateFavoriteIdol(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @Valid @RequestBody UpdateFavoriteIdolRequest updateFavoriteIdolRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        userService.updateFavoriteIdol(loggedInUserId, updateFavoriteIdolRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
