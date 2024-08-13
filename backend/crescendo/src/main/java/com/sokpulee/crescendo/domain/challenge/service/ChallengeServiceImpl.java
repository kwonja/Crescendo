package com.sokpulee.crescendo.domain.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokpulee.crescendo.domain.alarm.service.AlarmService;
import com.sokpulee.crescendo.domain.challenge.dto.ai.Landmark;
import com.sokpulee.crescendo.domain.challenge.dto.ai.LandmarkFrame;
import com.sokpulee.crescendo.domain.challenge.dto.ai.LandmarkResponse;
import com.sokpulee.crescendo.domain.challenge.dto.request.CreateDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.request.JoinDanceChallengeRequest;
import com.sokpulee.crescendo.domain.challenge.dto.response.ChallengeVideoResponse;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeJoinResponse;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoinLike;
import com.sokpulee.crescendo.domain.challenge.entity.LandmarkPosition;
import com.sokpulee.crescendo.domain.challenge.repository.DanceChallengeJoinLikeRepository;
import com.sokpulee.crescendo.domain.challenge.repository.LandmarkPositionRepository;
import com.sokpulee.crescendo.domain.challenge.repository.dancechallengejoin.DanceChallengeJoinRepository;
import com.sokpulee.crescendo.domain.challenge.repository.dancechallenge.DanceChallengeRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final DanceChallengeRepository danceChallengeRepository;
    private final DanceChallengeJoinRepository danceChallengeJoinRepository;
    private final DanceChallengeJoinLikeRepository danceChallengeJoinLikeRepository;
    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;
    private final AlarmService alarmService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LandmarkPositionRepository landmarkPositionRepository;

    @Value("${ai.url}")
    private String aiUrl;

    @Override
    public void createChallenge(Long loggedInUserId, CreateDanceChallengeRequest createDanceChallengeRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        String saveDanceChallengeVideoPath = fileSaveHelper.saveDanceChallengeVideo(createDanceChallengeRequest.getVideo());

        DanceChallenge danceChallenge = createDanceChallengeRequest.toEntity(user, saveDanceChallengeVideoPath);

        danceChallengeRepository.save(danceChallenge);

        // API 요청 데이터 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("video_url", saveDanceChallengeVideoPath);

        // HTTP 요청 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        String apiUrl = aiUrl + "/api/v1/challenge/similarity/landmark";
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                LandmarkResponse landmarkResponse = objectMapper.readValue(responseBody, LandmarkResponse.class);

                saveLandmarkPositions(danceChallenge, landmarkResponse);
            } catch (Exception e) {
                throw new RuntimeException("Failed to process landmark data", e);
            }
        } else {
            throw new RuntimeException("Failed to get landmark data from Flask server");
        }
    }

    private void saveLandmarkPositions(DanceChallenge danceChallenge, LandmarkResponse landmarkResponse) {
        List<LandmarkPosition> landmarkPositions = new ArrayList<>();

        for (LandmarkFrame frame : landmarkResponse.getLandmarkPositions()) {
            for (Landmark landmark : frame.getLandmarks()) {
                LandmarkPosition position = new LandmarkPosition(
                        danceChallenge,
                        frame.getFrameNumber(),
                        landmark.getLandmarkIndex(),
                        landmark.getX(),
                        landmark.getY(),
                        landmark.getZ(),
                        landmark.getVisibility()
                );

                landmarkPositions.add(position);
            }
        }

        landmarkPositionRepository.saveAll(landmarkPositions);
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

            DanceChallengeJoin.DanceChallengeJoinBuilder builder = DanceChallengeJoin.builder()
                    .user(user)
                    .danceChallenge(danceChallenge)
                    .videoPath(saveDanceChallengeVideo);

            double score = analyzeChallengeSimilarity(danceChallenge, saveDanceChallengeVideo);

            DanceChallengeJoin danceChallengeJoin = builder.score(score)
                    .build();

            danceChallengeJoinRepository.save(danceChallengeJoin);

            alarmService.challengeJoinAlarm(danceChallenge.getTitle(), danceChallenge.getUser().getId(), loggedInUserId, challengeId);
        }
    }

    private double analyzeChallengeSimilarity(DanceChallenge danceChallenge, String videoUrl) {
        List<LandmarkPosition> landmarkPositions = landmarkPositionRepository.findByDanceChallenge(danceChallenge);

        List<Map<String, Object>> frames = new ArrayList<>();
        for (LandmarkPosition position : landmarkPositions) {
            Map<String, Object> landmarkData = new HashMap<>();
            landmarkData.put("frame_number", position.getFrameNumber());
            landmarkData.put("landmark_index", position.getLandmarkIndex());
            landmarkData.put("x", position.getX());
            landmarkData.put("y", position.getY());
            landmarkData.put("z", position.getZ());
            landmarkData.put("visibility", position.getVisibility());

            Map<String, Object> frameData = frames.stream()
                    .filter(frame -> frame.get("frame_number").equals(position.getFrameNumber()))
                    .findFirst()
                    .orElse(null);

            if (frameData == null) {
                frameData = new HashMap<>();
                frameData.put("frame_number", position.getFrameNumber());
                frameData.put("landmarks", new ArrayList<Map<String, Object>>());
                frames.add(frameData);
            }

            ((List<Map<String, Object>>) frameData.get("landmarks")).add(landmarkData);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("landmark_positions", frames);
        requestBody.put("video_url", videoUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String apiUrl = aiUrl + "/api/v1/challenge/similarity/analyze";
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                // JSON 응답을 파싱하여 유사도 점수를 추출
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

                // similarity 값을 Number로 받아서 처리
                Number similarityScore = (Number) responseMap.get("similarity");
                return similarityScore.doubleValue();  // double로 변환하여 반환
            } catch (Exception e) {
                throw new RuntimeException("Failed to process similarity score", e);
            }
        } else {
            throw new RuntimeException("Failed to get similarity score from Flask server");
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

            alarmService.challengeJoinLikeAlarm(danceChallengeJoin.getDanceChallenge().getTitle(), danceChallengeJoin.getUser().getId(), loggedInUserId, danceChallengeJoin.getDanceChallenge().getId());
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

    @Override
    public Page<GetDanceChallengeJoinResponse> getChallengeJoins(Long challengeId, String nickname, String sortBy, Long loggedInUserId, Pageable pageable) {
        return danceChallengeJoinRepository.searchChallengeJoins(challengeId, nickname, sortBy, loggedInUserId, pageable);
    }

    @Override
    public ChallengeVideoResponse getChallengeById(Long challengeId) {

        DanceChallenge danceChallenge = danceChallengeRepository.findById(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);

        return new ChallengeVideoResponse(danceChallenge.getVideoPath());
    }
}
