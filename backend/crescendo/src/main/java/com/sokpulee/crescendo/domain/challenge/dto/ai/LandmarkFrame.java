package com.sokpulee.crescendo.domain.challenge.dto.ai;

import lombok.Getter;

import java.util.List;

@Getter
public class LandmarkFrame {

    private int frameNumber;
    private List<Landmark> landmarks;

    public LandmarkFrame(int frameNumber, List<Landmark> landmarks) {
        this.frameNumber = frameNumber;
        this.landmarks = landmarks;
    }
}
