package com.sokpulee.crescendo.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IdolGroupDetailResponse {

    private Long idolGroupId;
    private String name;
    private int peopleNum;
    private String introduction;
    private String profile;
    private String banner;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFavorite;

    @Builder
    public IdolGroupDetailResponse(Long idolGroupId, String name, int peopleNum, String introduction, String profile, String banner, Boolean isFavorite) {
        this.idolGroupId = idolGroupId;
        this.name = name;
        this.peopleNum = peopleNum;
        this.introduction = introduction;
        this.profile = profile;
        this.banner = banner;
        this.isFavorite = isFavorite;
    }
}
