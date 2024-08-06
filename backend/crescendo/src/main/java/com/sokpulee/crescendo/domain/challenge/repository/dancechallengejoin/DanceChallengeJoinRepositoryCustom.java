package com.sokpulee.crescendo.domain.challenge.repository.dancechallengejoin;

import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeJoinResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DanceChallengeJoinRepositoryCustom {

    Page<GetDanceChallengeJoinResponse> searchChallengeJoins(Long challengeId, String nickname, String sortBy, Long userId, Pageable pageable);
}
