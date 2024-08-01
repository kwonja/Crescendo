package com.sokpulee.crescendo.domain.goods.controller;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;
import com.sokpulee.crescendo.domain.goods.service.GoodsService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

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
    @Operation(summary = "굿즈 댓글삭제", description = "굿즈 댓글삭제 API")
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











}
