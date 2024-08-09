package com.sokpulee.crescendo.domain.community.controller;

import com.sokpulee.crescendo.domain.community.dto.response.FavoritesGetResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupDetailResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupGetResponse;
import com.sokpulee.crescendo.domain.community.service.CommunityService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
@Tag(name = "Community", description = "커뮤니티 관련 API")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/favorites/idol-group/{idolGroupId}")
    @Operation(summary = "즐겨찾기", description = "즐겨찾기 API")
    public ResponseEntity<Void> toggleFavorite(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable Long idolGroupId
    ) {

        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        communityService.toggleFavorite(idolGroupId, loggedInUserId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/favorites")
    @Operation(summary = "내 즐겨찾기 조회", description = "내 즐겨찾기 조회 API")
    public ResponseEntity<?> getFavorites(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FavoritesGetResponse> favorites = communityService.getFavorites(loggedInUserId, pageable);

        return ResponseEntity.status(OK).body(favorites);
    }

    @GetMapping
    @Operation(summary = "커뮤니티 아이돌 그룹 조회", description = "커뮤니티 아이돌 그룹 조회 API")
    public Page<IdolGroupGetResponse> getIdolGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return communityService.getIdolGroups(keyword, pageable);
    }

    @GetMapping("/idol-group/{idol-group-id}")
    @Operation(summary = "커뮤니티 아이돌 그룹 상세 조회", description = "커뮤니티 아이돌 그룹 상세 조회 API")
    public IdolGroupDetailResponse getIdolGroupDetail(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("idol-group-id") Long idolGroupId
    ) {
        return communityService.getIdolGroupDetail(loggedInUserId, idolGroupId);
    }
}
