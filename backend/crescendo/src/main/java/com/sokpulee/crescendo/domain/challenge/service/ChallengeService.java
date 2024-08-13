package com.sokpulee.crescendo.domain.challenge.service;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.response.ChallengeVideoResponse;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeJoinResponse;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeService {
    void createChallenge(Long loggedInUserId, CreateDanceChallengeRequest createDanceChallengeRequest);

    void joinChallenge(Long loggedInUserId, Long challengeId, JoinDanceChallengeRequest joinDanceChallengeRequest);

    void likeChallengeJoin(Long loggedInUserId, Long challengeJoinId);

    void deleteChallenge(Long loggedInUserId, Long challengeId);

    void deleteChallengeJoin(Long loggedInUserId, Long challengeJoinId);

    Page<GetDanceChallengeResponse> getChallenges(String title, String sortBy, Pageable pageable);

    Page<GetDanceChallengeJoinResponse> getChallengeJoins(Long challengeId, String nickname, String sortBy, Long loggedInUserId, Pageable pageable);

    ChallengeVideoResponse getChallengeById(Long challengeId);
}
