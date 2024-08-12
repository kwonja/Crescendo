package com.sokpulee.crescendo.domain.challenge.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LandmarkResponse {

    @JsonProperty("landmark_positions")
    private List<LandmarkFrame> landmarkPositions =  new ArrayList<>();

    public LandmarkResponse(List<LandmarkFrame> landmarkPositions) {
        this.landmarkPositions = landmarkPositions;
    }
}
