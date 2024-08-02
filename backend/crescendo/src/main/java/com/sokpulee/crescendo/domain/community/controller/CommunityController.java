package com.sokpulee.crescendo.domain.community.controller;

import com.sokpulee.crescendo.domain.community.service.CommunityFavoritesService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityFavoritesService communityFavoritesService;

    @PostMapping("/idol-group/{idolGroupId}")
    public ResponseEntity<Void> toggleFavorite(
            @AuthPrincipal Long loggedInUserId,
            @PathVariable Long idolGroupId
    ) {

        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        communityFavoritesService.toggleFavorite(idolGroupId, loggedInUserId);
        return ResponseEntity.ok().build();
    }
}
