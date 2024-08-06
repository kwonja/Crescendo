package com.sokpulee.crescendo.domain.challenge.service;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoinLike;
import com.sokpulee.crescendo.domain.challenge.repository.DanceChallengeJoinLikeRepository;
import com.sokpulee.crescendo.domain.challenge.repository.DanceChallengeJoinRepository;
import com.sokpulee.crescendo.domain.challenge.repository.dancechallenge.DanceChallengeRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final DanceChallengeRepository danceChallengeRepository;
    private final DanceChallengeJoinRepository danceChallengeJoinRepository;
    private final DanceChallengeJoinLikeRepository danceChallengeJoinLikeRepository;
    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;

    @Override
    public void createChallenge(Long loggedInUserId, CreateDanceChallengeRequest createDanceChallengeRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        String saveDanceChallengeVideoPath = fileSaveHelper.saveDanceChallengeVideo(createDanceChallengeRequest.getVideo());

        DanceChallenge danceChallenge = createDanceChallengeRequest.toEntity(user, saveDanceChallengeVideoPath);

        danceChallengeRepository.save(danceChallenge);
    }

    @Override
    public void joinChallenge(Long loggedInUserId, Long challengeId, JoinDanceChallengeRequest joinDanceChallengeRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        DanceChallenge danceChallenge = danceChallengeRepository.findById(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);

        Optional<DanceChallengeJoin> byDanceChallengeAndUser = danceChallengeJoinRepository.findByDanceChallengeAndUser(danceChallenge, user);

        if(byDanceChallengeAndUser.isPresent()) {
            throw new DanceChallengeJoinConflictException();
        }
        else {
            String saveDanceChallengeVideo = fileSaveHelper.saveDanceChallengeVideo(joinDanceChallengeRequest.getVideo());

            danceChallengeJoinRepository.save(DanceChallengeJoin.builder()
                    .user(user)
                    .danceChallenge(danceChallenge)
                    .videoPath(saveDanceChallengeVideo)
                    .build());
        }
    }

    @Override
    public void likeChallengeJoin(Long loggedInUserId, Long challengeJoinId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        DanceChallengeJoin danceChallengeJoin = danceChallengeJoinRepository.findById(challengeJoinId)
                .orElseThrow(DanceChallengeJoinNotFoundException::new);

        Optional<DanceChallengeJoinLike> danceChallengeJoinLike = danceChallengeJoinLikeRepository.findByDanceChallengeJoinAndUser(danceChallengeJoin, user);

        if(danceChallengeJoinLike.isPresent()) {
            danceChallengeJoinLikeRepository.delete(danceChallengeJoinLike.get());
        }
        else {
            danceChallengeJoinLikeRepository.save(DanceChallengeJoinLike.builder()
                            .user(user)
                            .danceChallengeJoin(danceChallengeJoin)
                            .build());
        }

    }

    @Override
    public void deleteChallenge(Long loggedInUserId, Long challengeId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        DanceChallenge danceChallenge = danceChallengeRepository.findByIdWithUser(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);

        if(!danceChallenge.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }
        danceChallengeRepository.delete(danceChallenge);

    }

    @Override
    public void deleteChallengeJoin(Long loggedInUserId, Long challengeJoinId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        DanceChallengeJoin danceChallengeJoin = danceChallengeJoinRepository.findByIdWithUser(challengeJoinId)
                .orElseThrow(DanceChallengeJoinNotFoundException::new);

        if(!danceChallengeJoin.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }

        danceChallengeJoinRepository.delete(danceChallengeJoin);
    }

    @Override
    public Page<GetDanceChallengeResponse> getChallenges(String title, String sortBy, Pageable pageable) {
        return danceChallengeRepository.searchChallenges(title, sortBy, pageable);
    }
}
