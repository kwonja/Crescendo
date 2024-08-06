package com.sokpulee.crescendo.domain.challenge.service;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;

public interface ChallengeService {
    void createChallenge(Long loggedInUserId, CreateDanceChallengeRequest createDanceChallengeRequest);

    void joinChallenge(Long loggedInUserId, Long challengeId, JoinDanceChallengeRequest joinDanceChallengeRequest);

    void likeChallengeJoin(Long loggedInUserId, Long challengeJoinId);

    void deleteChallenge(Long loggedInUserId, Long challengeId);
}
