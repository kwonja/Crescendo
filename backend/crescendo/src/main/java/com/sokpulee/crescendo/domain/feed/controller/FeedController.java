package com.sokpulee.crescendo.domain.feed.controller;

import com.sokpulee.crescendo.domain.feed.dto.request.*;
import com.sokpulee.crescendo.domain.feed.dto.response.*;
import com.sokpulee.crescendo.domain.feed.service.FeedService;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsSearchCondition;
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
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> imageList,
            @RequestParam(required = false) List<String> tagList,
            @RequestParam Long idolGroupId
            ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FeedAddRequest feedAddRequest = new FeedAddRequest(content,imageList,tagList,idolGroupId);

        feedService.addFeed(loggedInUserId, feedAddRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(summary = "피드 조회",description = "피드 조회 API")
    public ResponseEntity<Page<FeedResponse>> getFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam("idol-group-id") Long idolGroupId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) boolean sortByFollowed,
            @RequestParam(required = false) boolean sortByLiked
    ){
        Pageable pageable = PageRequest.of(page,size);

        FeedSearchCondition condition = FeedSearchCondition.builder()
                .nickname(nickname)
                .content(content)
                .sortByFollowed(sortByFollowed)
                .sortByLiked(sortByLiked)
                .build();

        Page<FeedResponse> feedResponses = feedService.getFeed(loggedInUserId,idolGroupId,pageable,condition);

        return ResponseEntity.ok(feedResponses);
    }

    @GetMapping("/{feed-id}")
    @Operation(summary = "피드 상세조회", description = "피드 상세조회 API")
    public ResponseEntity<FeedDetailResponse> getFeedDetail(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId
    ){
        FeedDetailResponse feedDetailResponse = feedService.getFeedDetail(loggedInUserId, feedId);

        return ResponseEntity.ok(feedDetailResponse);
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
        feedService.deleteFeed(feedId,loggedInUserId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{feed-id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "피드 글수정", description = "피드 글수정 API")
    public ResponseEntity<?> updateFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @Valid @ModelAttribute FeedUpdateRequest feedUpdateRequest
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        feedService.updateFeed(loggedInUserId,feedId,feedUpdateRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-feed")
    @Operation(summary = "내가 쓴 피드", description = "내가 쓴 피드 API")
    public ResponseEntity<Page<MyFeedResponse>> getMyFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        Pageable pageable = PageRequest.of(page,size);

        Page<MyFeedResponse> myFeedResponses = feedService.getMyFeed(loggedInUserId,pageable);

        return ResponseEntity.ok(myFeedResponses);
    }

    @GetMapping("/user/{user-id}")
    @Operation(summary = "특정 회원이 쓴 피드 조회", description = "특정 회원이 쓴 피드 조회 API")
    public ResponseEntity<Page<GetFeedByUserIdResponse>> getFeedByUserId(
            @PathVariable("user-id") Long userId,
            @RequestParam int page,
            @RequestParam int size
    ){

        Pageable pageable = PageRequest.of(page,size);

        Page<GetFeedByUserIdResponse> myFeedResponses = feedService.getFeedByUserId(userId, pageable);

        return ResponseEntity.ok(myFeedResponses);
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

    @GetMapping("/{feed-id}/comment")
    @Operation(summary = "피드 댓글조회", description = "피드 댓글조회 API")
    public ResponseEntity<Page<FeedCommentResponse>> getFeedComment(
        @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
        @PathVariable("feed-id") Long feedId,
        @RequestParam int page,
        @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page,size);

        Page<FeedCommentResponse> feedCommentResponses = feedService.getFeedComment(loggedInUserId,feedId,pageable);

        return ResponseEntity.ok(feedCommentResponses);
    }

    @DeleteMapping("/{feed-id}/comment/{feed-comment-id}")
    @Operation(summary = "피드 댓글 및 답글 삭제", description = "피드 댓글 및 답글 삭제 API")
    public ResponseEntity<?> deleteFeedComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @PathVariable("feed-comment-id") Long feedCommentId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        feedService.deleteFeedComment(loggedInUserId,feedId,feedCommentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{feed-id}/comment/{feed-comment-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "피드 댓글 및 답글 수정", description = "피드 댓글 및 답글 수정 API")
    public ResponseEntity<?> updateFeedComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @PathVariable("feed-comment-id") Long feedCommentId,
            @ModelAttribute FeedCommentUpdateRequest feedCommentUpdateRequest
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        feedService.updateFeedComment(loggedInUserId, feedId, feedCommentId, feedCommentUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{feed-id}/comment/{feed-comment-id}/reply")
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

    @GetMapping("/{feed-id}/comment/{feed-comment-id}/reply")
    @Operation(summary = "피드 답글조회", description = "피드 답글조회 API")
    public ResponseEntity<Page<FeedReplyResponse>> getFeedReply(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId,
            @PathVariable("feed-comment-id") Long feedCommentId,
            @RequestParam int page,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page,size);

        Page<FeedReplyResponse> feedReplyResponses = feedService.getFeedReply(loggedInUserId,feedId,feedCommentId,pageable);

        return ResponseEntity.ok(feedReplyResponses);
    }

    @PostMapping("/feed-like/{feed-id}")
    @Operation(summary = "피드 좋아요 및 좋아요 삭제", description = "피드 좋아요 및 좋아요 삭제 API")
    public ResponseEntity<?> likeFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-id") Long feedId
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        feedService.likeFeed(loggedInUserId,feedId);

        return ResponseEntity.status(OK).build();
    }

    @PostMapping("/feed-comment-like/{feed-comment-id}")
    @Operation(summary = "피드 댓글 및 답글 좋아요 & 좋아요 삭제", description = "피드 댓글 및 답글 좋아요 & 좋아요 삭제 API")
    public ResponseEntity<?> likeFeedComment(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("feed-comment-id") Long feedCommentId
    ){
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        feedService.likeFeedComment(loggedInUserId,feedCommentId);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/favorite")
    @Operation(summary = "좋아요한 피드 조회", description = "좋아요한 피드 조회 API")
    public ResponseEntity<Page<FavoriteFeedResponse>> favoriteFeed(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(loggedInUserId == null){
            throw new AuthenticationRequiredException();
        }

        Pageable pageable = PageRequest.of(page,size);

        Page<FavoriteFeedResponse> favoriteFeeds = feedService.getFavoriteFeed(loggedInUserId,pageable);

        return ResponseEntity.ok(favoriteFeeds);
    }
}
