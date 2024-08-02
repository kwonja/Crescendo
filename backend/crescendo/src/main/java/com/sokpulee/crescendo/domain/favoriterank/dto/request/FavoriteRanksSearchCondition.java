package com.sokpulee.crescendo.domain.favoriterank.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteRanksSearchCondition {

    Long idolId;
    boolean sortByVotes;

    @Builder
    public FavoriteRanksSearchCondition(Long idolId, boolean sortByVotes) {
        this.idolId = idolId;
        this.sortByVotes = sortByVotes;
    }
}
