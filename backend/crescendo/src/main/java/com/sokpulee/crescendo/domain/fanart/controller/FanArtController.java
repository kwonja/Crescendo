package com.sokpulee.crescendo.domain.fanart.controller;

import com.sokpulee.crescendo.domain.fanart.dto.request.*;
import com.sokpulee.crescendo.domain.fanart.dto.response.*;
import com.sokpulee.crescendo.domain.fanart.service.FanArtService;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.response.*;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.response.GetGoodsByUserIdResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/fan-art")
@Tag(name = "FanArt", description = "팬아트 관련 API")
public class FanArtController {

    private final FanArtService fanArtService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팬아트 글쓰기", description = "팬아트 글쓰기 API")
    public ResponseEntity<?> addFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList,
            @RequestParam("idolGroupId") Long idolGroupId
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtAddRequest fanArtAddRequest = new FanArtAddRequest(title, content, imageList, idolGroupId);

        fanArtService.addFanArt(loggedInUserId, fanArtAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(summary = "팬아트 조회", description = "팬아트 조회 API")
    public ResponseEntity<Page<FanArtResponse>> getFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam("idol-group-id") Long idolGroupId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String content
    ){

        FanArtSearchCondition condition = FanArtSearchCondition.builder()
                .title(title)
                .nickname(nickname)
                .content(content)
                .build();

        Pageable pageable = PageRequest.of(page, size);

        Page<FanArtResponse> fanArtResponsePage = fanArtService.getFanArt(loggedInUserId,idolGroupId,pageable,condition);

        return ResponseEntity.ok(fanArtResponsePage);
    }

    @GetMapping("/{fan-art-id}")
    @Operation(summary = "팬아트 상세조회", description = "팬아트 상세조회 API")
    public ResponseEntity<FanArtDetailResponse> getFanArtDetail(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId
    ){
        FanArtDetailResponse fanArtDetailResponse = fanArtService.getFanArtDetail(loggedInUserId, fanArtId);

        return ResponseEntity.ok(fanArtDetailResponse);
    }

    @DeleteMapping("/{fan-art-id}")
    @Operation(summary = "팬아트 글삭제", description = "팬아트 글삭제 API")
    public ResponseEntity<?> deleteFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        fanArtService.deleteFanArt(fanArtId, loggedInUserId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{fan-art-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팬아트 글수정", description = "팬아트 글수정 API")
    public ResponseEntity<?> updateFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @Valid @ModelAttribute FanArtUpdateRequest updateRequest
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        fanArtService.updateFanArt(loggedInUserId, fanArtId, updateRequest);

        return ResponseEntity.ok().build();

    }


    @PostMapping("/{fan-art-id}/comment")
    @Operation(summary = "팬아트 댓글쓰기", description = "팬아트 댓글쓰기 API")
    public ResponseEntity<?> addFanArtComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @RequestParam String content
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtCommentAddRequest fanArtCommentAddRequest = new FanArtCommentAddRequest(content);

        fanArtService.addFanArtComment(loggedInUserId, fanArtId, fanArtCommentAddRequest);

        return ResponseEntity.status(CREATED).build();
    }


    @GetMapping("/{fan-art-id}/comment")
    @Operation(summary = "팬아트 댓글조회", description = "팬아트 댓글조회 API")
    public ResponseEntity<Page<FanArtCommentResponse>> getFanArtComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @RequestParam int page,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page,size);

        Page<FanArtCommentResponse> fanArtCommentResponses = fanArtService.getFanArtComment(loggedInUserId,fanArtId,pageable);

        return ResponseEntity.ok(fanArtCommentResponses);
    }

    @DeleteMapping("/{fan-art-id}/comment/{fan-art-comment-id}")
    @Operation(summary = "팬아트 댓글 및 답글 삭제", description = "팬아트 댓글 및 답글 삭제 API")
    public ResponseEntity<?> deleteGoodsComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        fanArtService.deleteFanArtComment(loggedInUserId, fanArtId, fanArtCommentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{fan-art-id}/comment/{fan-art-comment-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팬아트 댓글 및 답글 수정", description = "팬아트 댓글 및 답글 수정 API")
    public ResponseEntity<?> updateFanArtComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId,
            @ModelAttribute FanArtCommentUpdateRequest fanArtCommentUpdateRequest
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        fanArtService.updateFanArtComment(loggedInUserId, fanArtId, fanArtCommentId, fanArtCommentUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{fan-art-id}/comment/{fan-art-comment-id}/reply")
    @Operation(summary = "팬아트 답글쓰기", description = "팬아트 답글쓰기 API")
    public ResponseEntity<?> addFanArtReply(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId,
            @RequestParam String content
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtCommentAddRequest fanArtReplyAddRequest = new FanArtCommentAddRequest(content);

        fanArtService.addFanArtReply(loggedInUserId, fanArtId, fanArtCommentId, fanArtReplyAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/{fan-art-id}/comment/{fan-art-comment-id}/reply")
    @Operation(summary = "팬아트 답글조회", description = "팬아트 답글조회 API")
    public ResponseEntity<Page<FanArtReplyResponse>> getFanArtReply(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId,
            @RequestParam int page,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page,size);

        Page<FanArtReplyResponse> fanArtReply = fanArtService.getFanArtReply(loggedInUserId,fanArtId,fanArtCommentId,pageable);

        return ResponseEntity.ok(fanArtReply);
    }

    @PostMapping("/fan-art-like/{fan-art-id}")
    @Operation(summary = "팬아트 좋아요 및 좋아요 삭제", description = "팬아트 좋아요 및 좋아요 삭제 API")
    public ResponseEntity<?> addFanArtLike(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        fanArtService.likeFanArt(loggedInUserId,fanArtId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorite")
    @Operation(summary = "좋아요한 팬아트 조회", description = "좋아요한 팬아트 조회 API")
    public ResponseEntity<Page<FavoriteFanArtResponse>> favoriteFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        Pageable pageable = PageRequest.of(page,size);

        Page<FavoriteFanArtResponse> favoriteFanArtResponses = fanArtService.getFavoriteFanArt(loggedInUserId,pageable);

        return ResponseEntity.ok(favoriteFanArtResponses);
    }

    @GetMapping("/my-fan-art")
    @Operation(summary = "내가 쓴 팬아트", description = "내가 쓴 팬아트 API")
    public ResponseEntity<Page<MyFanArtResponse>> getMyFanArt(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        Pageable pageable = PageRequest.of(page,size);

        Page<MyFanArtResponse> myFanArtResponses = fanArtService.getMyFanArt(loggedInUserId,pageable);

        return ResponseEntity.ok(myFanArtResponses);
    }

    @PostMapping("/fan-art-comment-like/{fan-art-comment-id}")
    @Operation(summary = "팬아트 댓글 및 답글 좋아요 & 좋아요 삭제", description = "팬아트 댓글 및 답글 좋아요 & 좋아요 삭제 API")
    public ResponseEntity<?> likeFanArtComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        fanArtService.likeFanArtComment(loggedInUserId,fanArtCommentId);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/user/{user-id}")
    @Operation(summary = "특정 회원이 쓴 팬아트 조회", description = "특정 회원이 쓴 팬아트 조회 API")
    public ResponseEntity<Page<GetFanArtByUserIdResponse>> getFanArtByUserId(
            @PathVariable("user-id") Long userId,
            @RequestParam int page,
            @RequestParam int size
    ){

        Pageable pageable = PageRequest.of(page,size);

        Page<GetFanArtByUserIdResponse> myFanArtResponses = fanArtService.getFanArtByUserId(userId, pageable);

        return ResponseEntity.ok(myFanArtResponses);
    }

}
