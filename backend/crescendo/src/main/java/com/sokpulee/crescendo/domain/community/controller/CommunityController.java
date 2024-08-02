package com.sokpulee.crescendo.domain.community.controller;

import com.sokpulee.crescendo.domain.community.dto.response.FavoritesGetResponse;
import com.sokpulee.crescendo.domain.community.service.CommunityFavoritesService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Tag(name = "Community", description = "커뮤니티 관련 API")
public class CommunityController {

    private final CommunityFavoritesService communityFavoritesService;

    @PostMapping("/idol-group/{idolGroupId}")
    @Operation(summary = "즐겨찾기", description = "즐겨찾기 API")
    public ResponseEntity<Void> toggleFavorite(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable Long idolGroupId
    ) {

        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        communityFavoritesService.toggleFavorite(idolGroupId, loggedInUserId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/favorites")
    @Operation(summary = "내 즐겨찾기 조회", description = "내 즐겨찾기 조회 API")
    public ResponseEntity<?> getFavorites(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FavoritesGetResponse> favorites = communityFavoritesService.getFavorites(loggedInUserId, pageable);

        return ResponseEntity.status(OK).body(favorites);
    }
}
