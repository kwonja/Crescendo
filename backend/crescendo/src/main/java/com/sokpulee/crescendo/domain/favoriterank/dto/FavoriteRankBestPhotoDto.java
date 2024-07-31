package com.sokpulee.crescendo.domain.favoriterank.dto;

public class FavoriteRankBestPhotoDto {

    private Long idolId;
    private String idolName;
    private String favoriteIdolImagePath;

    public FavoriteRankBestPhotoDto(Long idolId, String idolName, String favoriteIdolImagePath) {
        this.idolId = idolId;
        this.idolName = idolName;
        this.favoriteIdolImagePath = favoriteIdolImagePath;
    }
}
