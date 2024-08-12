package com.sokpulee.crescendo.domain.challenge.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LandmarkFrame {

    @JsonProperty("frame_number")
    private int frameNumber;
    private List<Landmark> landmarks;

    public LandmarkFrame(int frameNumber, List<Landmark> landmarks) {
        this.frameNumber = frameNumber;
        this.landmarks = landmarks;
    }
}
