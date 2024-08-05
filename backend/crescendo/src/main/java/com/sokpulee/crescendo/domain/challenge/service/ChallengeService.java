package com.sokpulee.crescendo.domain.challenge.service;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;

public interface ChallengeService {
    void createChallenge(Long loggedInUserId, CreateDanceChallengeRequest createDanceChallengeRequest);
}
