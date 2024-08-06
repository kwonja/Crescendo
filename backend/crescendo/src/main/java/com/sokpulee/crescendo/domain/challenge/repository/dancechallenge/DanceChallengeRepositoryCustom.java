package com.sokpulee.crescendo.domain.challenge.repository.dancechallenge;

import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DanceChallengeRepositoryCustom {

    Page<GetDanceChallengeResponse> searchChallenges(String title, String sortBy, Pageable pageable);
}
