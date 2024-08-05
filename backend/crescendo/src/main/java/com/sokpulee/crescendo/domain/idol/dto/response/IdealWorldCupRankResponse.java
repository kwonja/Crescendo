package com.sokpulee.crescendo.domain.idol.dto.response;

import lombok.Getter;

@Getter
public class IdealWorldCupRankResponse {
    private int rank;
    private Long idolId;
    private String idolGroupName;
    private String idolName;
    private String idolProfile2Path;
    private int winNum;

    public IdealWorldCupRankResponse(int rank, Long idolId, String idolGroupName, String idolName, String idolProfile2Path, int winNum) {
        this.rank = rank;
        this.idolId = idolId;
        this.idolGroupName = idolGroupName;
        this.idolName = idolName;
        this.idolProfile2Path = idolProfile2Path;
        this.winNum = winNum;
    }
}
