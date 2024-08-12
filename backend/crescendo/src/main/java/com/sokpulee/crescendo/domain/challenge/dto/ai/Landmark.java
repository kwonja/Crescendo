package com.sokpulee.crescendo.domain.challenge.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Landmark {

    @JsonProperty("landmark_index")
    private int landmarkIndex;
    private double x;
    private double y;
    private double z;
    private double visibility;

    public Landmark(int landmarkIndex, double x, double y, double z, double visibility) {
        this.landmarkIndex = landmarkIndex;
        this.x = x;
        this.y = y;
        this.z = z;
        this.visibility = visibility;
    }
}
