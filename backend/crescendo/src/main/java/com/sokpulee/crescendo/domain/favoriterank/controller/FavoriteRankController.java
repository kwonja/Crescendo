package com.sokpulee.crescendo.domain.favoriterank.controller;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRankAddRequest;
import com.sokpulee.crescendo.domain.favoriterank.service.FavoriteRankService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite_rank")
@Tag(name = "FavoriteRank", description = "전국 최애 자랑 관련 API")
public class FavoriteRankController {

    private final FavoriteRankService favoriteRankService;

    @PostMapping
    public ResponseEntity<?> registerFavoriteRank(
            @AuthPrincipal Long loggedInUserId,
            @Valid @ModelAttribute FavoriteRankAddRequest favoriteRankAddRequest
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        favoriteRankService.registerFavoriteRank(loggedInUserId, favoriteRankAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
}
