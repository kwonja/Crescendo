package com.sokpulee.crescendo.domain.favoriterank.controller;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRankAddRequest;
import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankBestPhotoResponse;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import com.sokpulee.crescendo.domain.favoriterank.service.FavoriteRankService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
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
@RequestMapping("/api/v1/favorite-rank")
@Tag(name = "FavoriteRank", description = "전국 최애 자랑 관련 API")
public class FavoriteRankController {

    private final FavoriteRankService favoriteRankService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "전국 최애 자랑 등록", description = "전국 최애 자랑 등록 API")
    public ResponseEntity<?> registerFavoriteRank(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @Valid @ModelAttribute FavoriteRankAddRequest favoriteRankAddRequest
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        favoriteRankService.registerFavoriteRank(loggedInUserId, favoriteRankAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(summary = "전국 최애 자랑 조회", description = "전국 최애 자랑 조회 API")
    public ResponseEntity<Page<FavoriteRankResponse>> getFavoriteRanks(@RequestParam int page,
                                                                       @RequestParam int size,
                                                                       @RequestParam(required = false) Long idolId,
                                                                       @RequestParam(required = false) boolean sortByVotes,
                                                                       @Parameter(hidden = true) @AuthPrincipal Long userId) {
        Pageable pageable = PageRequest.of(page, size);

        FavoriteRanksSearchCondition condition = FavoriteRanksSearchCondition
                .builder()
                .idolId(idolId)
                .sortByVotes(sortByVotes)
                .build();
        Page<FavoriteRankResponse> favoriteRanks = favoriteRankService.getFavoriteRanks(userId, condition, pageable);
        return ResponseEntity.ok(favoriteRanks);
    }

    @DeleteMapping("/{favorite-rank-id}")
    @Operation(summary = "전국 최애 자랑 삭제", description = "전국 최애 자랑 삭제 API")
    public ResponseEntity<?> deleteFavoriteRank(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("favorite-rank-id") Long favoriteRankId
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        favoriteRankService.deleteFavoriteRank(loggedInUserId, favoriteRankId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/bestphoto")
    @Operation(summary = "전국 최애 자랑 아이돌 별 베스트 사진 조회", description = "전국 최애 자랑 아이돌 별 베스트 사진 조회 API")
    public ResponseEntity<?> getBestPhoto() {

        FavoriteRankBestPhotoResponse response = favoriteRankService.getBestPhoto();

        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("/{favorite-rank-id}/vote")
    @Operation(summary = "전국 최애 자랑 투표", description = "전국 최애 자랑 투표 API")
    public ResponseEntity<?> voteFavoriteRank(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("favorite-rank-id") Long favoriteRankId) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        favoriteRankService.voteFavoriteRank(loggedInUserId, favoriteRankId);

        return ResponseEntity.status(CREATED).build();
    }
}
