package com.sokpulee.crescendo.domain.favoriterank.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteRanksSearchCondition {

    Long idolId;
    Long idolGroupId;
    boolean sortByVotes;

    @Builder
    public FavoriteRanksSearchCondition(Long idolId, Long idolGroupId, boolean sortByVotes) {
        this.idolId = idolId;
        this.idolGroupId = idolGroupId;
        this.sortByVotes = sortByVotes;
    }
}
