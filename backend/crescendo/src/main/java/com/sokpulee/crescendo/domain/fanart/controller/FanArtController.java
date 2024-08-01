package com.sokpulee.crescendo.domain.fanart.controller;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentAddRequest;
import com.sokpulee.crescendo.domain.fanart.service.FanArtService;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/fan-art")
@Tag(name = "FanArt", description = "팬아트 관련 API")
public class FanArtController {

    private final FanArtService fanArtService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팬아트 글쓰기", description = "팬아트 글쓰기 API")
    public ResponseEntity<?> addFanArt(
            @AuthPrincipal Long loggedInUserId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "imageList",required = false) List<MultipartFile> imageList,
            @RequestParam("idolGroupId") Long idolGroupId
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtAddRequest fanArtAddRequest = new FanArtAddRequest(title,content,imageList,idolGroupId);

        fanArtService.addFanArt(loggedInUserId, fanArtAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
    @DeleteMapping("/{fan-art-id}")
    @Operation(summary = "팬아트 글삭제", description = "팬아트 글삭제 API")
    public ResponseEntity<?> deleteFanArt(
            @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        fanArtService.deleteFanArt(fanArtId);
        return ResponseEntity.noContent().build();
    }
    

    @PostMapping("/{fan-art-id}/comment")
    @Operation(summary = "팬아트 댓글쓰기", description = "팬아트 댓글쓰기 API")
    public ResponseEntity<?> addFanArtComment(
            @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @RequestParam String content
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtCommentAddRequest fanArtCommentAddRequest = new FanArtCommentAddRequest(content);

        fanArtService.addFanArtComment(loggedInUserId,fanArtId,fanArtCommentAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("{fan-art-id}/comment/{fan-art-comment-id}/reply")
    @Operation(summary = "팬아트 답글쓰기", description = "팬아트 답글쓰기 API")
    public ResponseEntity<?> addFanArtReply(
            @AuthPrincipal Long loggedInUserId,
            @PathVariable("fan-art-id") Long fanArtId,
            @PathVariable("fan-art-comment-id") Long fanArtCommentId,
            @RequestParam String content
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtCommentAddRequest fanArtReplyAddRequest = new FanArtCommentAddRequest(content);

        fanArtService.addFanArtReply(loggedInUserId,fanArtId,fanArtCommentId,fanArtReplyAddRequest);

        return ResponseEntity.status(CREATED).build();


    }

}
