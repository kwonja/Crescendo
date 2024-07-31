package com.sokpulee.crescendo.domain.favoriterank.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite_rank")
@Tag(name = "FavoriteRank", description = "전국 최애 자랑 관련 API")
public class FavoriteRankController {

}
