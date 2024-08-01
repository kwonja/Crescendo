package com.sokpulee.crescendo.domain.feed.controller;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.service.FeedService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/feed")
@Tag(name = "Feed", description = "피드 관련 API")
public class FeedController {

    private final FeedService feedService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "피드 글쓰기", description = "피드 글쓰기 API")
    public ResponseEntity<?> addFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> imageList,
            @RequestParam(required = false) List<String> tagList,
            @RequestParam Long idolGroupId
            ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FeedAddRequest feedAddRequest = new FeedAddRequest(title,content,imageList,tagList,idolGroupId);

        feedService.addFeed(loggedInUserId, feedAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
    
    @DeleteMapping("/{feed-id}")
    @Operation(summary = "피드 글삭제", description = "피드 글삭제 API")
    public ResponseEntity<?> deleteFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        feedService.deleteFeed(feedId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{feed-id}/comment")
    @Operation(summary = "피드 댓글쓰기", description = "피드 댓글쓰기 API")
    public ResponseEntity<?> addFeedComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @RequestParam String content
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }
        FeedCommentAddRequest feedCommentAddRequest = new FeedCommentAddRequest(content);

        feedService.addFeedComment(loggedInUserId, feedId, feedCommentAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("{feed-id}/comment/{feed-comment-id}/reply")
    @Operation(summary = "피드 답글쓰기", description = "피드 답글쓰기 API")
    public ResponseEntity<?> addFeedReply(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @PathVariable("feed-comment-id") Long feedCommentId,
            @RequestParam String content
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        FeedCommentAddRequest feedReplyAddRequest = new FeedCommentAddRequest(content);

        feedService.addFeedReply(loggedInUserId,feedId,feedCommentId,feedReplyAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
}
