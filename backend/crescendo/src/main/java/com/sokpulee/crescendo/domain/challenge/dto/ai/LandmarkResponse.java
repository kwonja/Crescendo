package com.sokpulee.crescendo.domain.challenge.dto.ai;

import lombok.Getter;

import java.util.List;

@Getter
public class LandmarkResponse {

    private List<LandmarkFrame> landmarkPositions;

    public LandmarkResponse(List<LandmarkFrame> landmarkPositions) {
        this.landmarkPositions = landmarkPositions;
    }
}
