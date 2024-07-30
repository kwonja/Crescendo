package com.sokpulee.crescendo.domain.feed.controller;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.service.FeedService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/feed")
@Tag(name = "Feed", description = "피드 관련 API")
public class FeedController {

    private final FeedService feedService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addFeed(
            @AuthPrincipal Long loggedInUserId,
            @Valid  @ModelAttribute FeedAddRequest feedAddRequest
    ) {
//        if(loggedInUserId == null) {
////            throw new AuthenticationRequi redException();
////        }
        System.out.println(feedAddRequest.toString());

        feedService.addFeed(loggedInUserId, feedAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
}
