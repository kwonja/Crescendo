package com.sokpulee.crescendo.domain.goods.controller;

import com.sokpulee.crescendo.domain.fanart.dto.response.FavoriteFanArtResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.MyFanArtResponse;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.MyGoodsResponse;
import com.sokpulee.crescendo.domain.goods.service.GoodsService;
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
@RequestMapping("/api/v1/community/goods")
@Tag(name = "Goods", description = "굿즈 관련 API")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "굿즈 글쓰기", description = "굿즈 글쓰기 API")
    public ResponseEntity<?> addGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> imageList,
            @RequestParam Long idolGroupId
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        GoodsAddRequest goodsAddRequest = new GoodsAddRequest(title, content, imageList, idolGroupId);
        goodsService.addGoods(loggedInUserId, goodsAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping("/{goods-id}")
    @Operation(summary = "굿즈 글삭제", description = "굿즈 글삭제 API")
    public ResponseEntity<?> deleteGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
        @PathVariable("goods-id") Long goodsId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        goodsService.deleteGoods(goodsId,loggedInUserId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{goods-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "굿즈 글수정", description = "굿즈 글수정 API")
    public ResponseEntity<?> updateGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId,
            @Valid @ModelAttribute GoodsUpdateRequest goodsUpdateRequest
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        goodsService.updateGoods(loggedInUserId,goodsId,goodsUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{goods-id}/comment")
    @Operation(summary = "굿즈 댓글쓰기", description = "굿즈 댓글쓰기 API")
    public ResponseEntity<?> addGoodsComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId,
            @RequestParam String content
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        GoodsCommentAddRequest goodsCommentAddRequest = new GoodsCommentAddRequest(content);

        goodsService.addGoodsComment(loggedInUserId,goodsId,goodsCommentAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping("/{goods-id}/comment/{goods-comment-id}")
    @Operation(summary = "굿즈 댓글 및 답글 삭제", description = "굿즈 댓글 및 답글 삭제 API")
    public ResponseEntity<?> deleteGoodsComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId,
            @PathVariable("goods-comment-id") Long goodsCommentId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        goodsService.deleteGoodsComment(loggedInUserId,goodsId,goodsCommentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "{goods-id}/comment/{goods-comment-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "굿즈 댓글 및 답글 수정", description = "굿즈 댓글 및 답글 수정 API")
    public ResponseEntity<?> updateGoodsComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId,
            @PathVariable("goods-comment-id") Long goodsCommentId,
            @ModelAttribute GoodsCommentUpdateRequest goodsCommentUpdateRequest
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        goodsService.updateGoodsComment(loggedInUserId,goodsId,goodsCommentId,goodsCommentUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("{goods-id}/comment/{goods-comment-id}/reply")
    @Operation(summary = "굿즈 답글쓰기", description = "굿즈 답글쓰기 API")
    public ResponseEntity<?> addGoodsReply(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId,
            @PathVariable("goods-comment-id") Long goodsCommentId,
            @RequestParam String content
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        GoodsCommentAddRequest goodsReplyAddRequest = new GoodsCommentAddRequest(content);

        goodsService.addGoodsReply(loggedInUserId,goodsId,goodsCommentId,goodsReplyAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/goods-like/{goods-id}")
    @Operation(summary = "굿즈 좋아요 및 좋아요 삭제", description = "굿즈 좋아요 및 좋아요 삭제 API")
    public ResponseEntity<?> likeGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("goods-id") Long goodsId
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        goodsService.likeGoods(loggedInUserId,goodsId);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/favorite")
    @Operation(summary = "좋아요한 굿즈 조회", description = "좋아요한 굿즈 조회 API")
    public ResponseEntity<Page<FavoriteGoodsResponse>> favoriteGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        Pageable pageable = PageRequest.of(page,size);

        Page<FavoriteGoodsResponse> favoriteGoodsResponses = goodsService.getFavoriteGoods(loggedInUserId,pageable);

        return ResponseEntity.ok(favoriteGoodsResponses);
    }

    @GetMapping("/my-goods")
    @Operation(summary = "내가 쓴 굿즈", description = "내가 쓴 굿즈 API")
    public ResponseEntity<Page<MyGoodsResponse>> getMyGoods(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        Pageable pageable = PageRequest.of(page,size);

        Page<MyGoodsResponse> myGoodsResponses = goodsService.getMyGoods(loggedInUserId,pageable);

        return ResponseEntity.ok(myGoodsResponses);
    }









}
