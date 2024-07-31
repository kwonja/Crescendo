package com.sokpulee.crescendo.domain.fanart.controller;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;
import com.sokpulee.crescendo.domain.fanart.service.FanArtService;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> addFanArt(
            @AuthPrincipal Long loggedInUserId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("imageList") List<MultipartFile> imageList,
            @RequestParam("idolGroupId") Long idolGroupId
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        FanArtAddRequest fanArtAddRequest = new FanArtAddRequest(title,content,imageList,idolGroupId);

        fanArtService.addFanArt(loggedInUserId, fanArtAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
}
