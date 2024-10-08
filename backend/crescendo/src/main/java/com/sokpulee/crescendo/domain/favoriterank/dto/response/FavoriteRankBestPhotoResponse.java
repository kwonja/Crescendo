package com.sokpulee.crescendo.domain.favoriterank.dto.response;

import com.sokpulee.crescendo.domain.favoriterank.dto.FavoriteRankBestPhotoDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FavoriteRankBestPhotoResponse {

    List<FavoriteRankBestPhotoDto> bestRankList;

    public FavoriteRankBestPhotoResponse(List<FavoriteRankBestPhotoDto> bestRankList) {
        this.bestRankList = bestRankList;
    }
}
