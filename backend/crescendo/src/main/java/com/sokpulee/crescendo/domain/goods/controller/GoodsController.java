package com.sokpulee.crescendo.domain.goods.controller;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.service.GoodsService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/community/goods")
@Tag(name = "Goods", description = "굿즈 관련 API")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "굿즈 글쓰기", description = "굿즈 글쓰기 API")
    public ResponseEntity<?> addGoods(
            @AuthPrincipal Long loggedInUserId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam List<MultipartFile> imageList,
            @RequestParam Long idolGroupId
    ) {
        if (loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        GoodsAddRequest goodsAddRequest = new GoodsAddRequest(title, content, imageList, idolGroupId);
        goodsService.addGoods(loggedInUserId, goodsAddRequest);

        return ResponseEntity.status(CREATED).build();
    }
}
