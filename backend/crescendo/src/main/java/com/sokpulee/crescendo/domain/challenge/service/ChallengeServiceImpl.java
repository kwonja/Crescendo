package com.sokpulee.crescendo.domain.challenge.service;

import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.challenge.repository.DanceChallengeJoinRepository;
import com.sokpulee.crescendo.domain.challenge.repository.DanceChallengeRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.ChallengeNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final DanceChallengeRepository danceChallengeRepository;
    private final DanceChallengeJoinRepository danceChallengeJoinRepository;
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

        String saveDanceChallengeVideo = fileSaveHelper.saveDanceChallengeVideo(joinDanceChallengeRequest.getVideo());

        danceChallengeJoinRepository.save(DanceChallengeJoin.builder()
                        .user(user)
                        .danceChallenge(danceChallenge)
                        .videoPath(saveDanceChallengeVideo)
                        .build());
    }
}
