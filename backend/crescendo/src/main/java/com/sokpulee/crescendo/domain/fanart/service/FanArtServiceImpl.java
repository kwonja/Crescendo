package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.alarm.service.AlarmService;
import com.sokpulee.crescendo.domain.fanart.dto.request.*;
import com.sokpulee.crescendo.domain.fanart.dto.response.*;
import com.sokpulee.crescendo.domain.fanart.entity.*;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtCommentLikeRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtCommentRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtLikeRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtRepository;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedDetailResponse;
import com.sokpulee.crescendo.domain.feed.entity.Feed;
import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import com.sokpulee.crescendo.domain.feed.entity.FeedCommentLike;
import com.sokpulee.crescendo.domain.feed.entity.FeedLike;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FanArtServiceImpl implements FanArtService {

    private final UserRepository userRepository;

    private final IdolGroupRepository idolGroupRepository;

    private final FileSaveHelper fileSaveHelper;

    private final FanArtRepository fanArtRepository;

    private final FanArtCommentRepository fanArtCommentRepository;

    private final FanArtLikeRepository fanArtLikeRepository;

    private final FanArtCommentLikeRepository fanArtCommentLikeRepository;

    private final AlarmService alarmService;

    @Override
    public void addFanArt(Long loggedInUserId, FanArtAddRequest fanArtAddRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);


        IdolGroup idolGroup = idolGroupRepository.findById(fanArtAddRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);


        FanArt fanArt = FanArt.builder()
                .idolGroup(idolGroup)
                .user(user)
                .title(fanArtAddRequest.getTitle())
                .content(fanArtAddRequest.getContent())
                .likeCnt(0)
                .commentCnt(0)
                .build();

        if (!fanArtAddRequest.getImageList().isEmpty()) {
            for (MultipartFile fanArtImageFile : fanArtAddRequest.getImageList()) {
                String savePath = fileSaveHelper.saveFanArtImage(fanArtImageFile);

                FanArtImage fanArtImage = FanArtImage.builder()
                        .imagePath(savePath)
                        .build();

                fanArt.addImage(fanArtImage);
            }
        }

        fanArtRepository.save(fanArt);
    }

    @Override
    public void deleteFanArt(Long fanArtId, Long loggedInUserId) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        if (!fanArt.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        if (fanArtRepository.existsById(fanArtId)) {
            fanArtRepository.deleteById(fanArtId);
        } else {
            throw new FanArtNotFoundException();
        }
    }

    @Override
    public void updateFanArt(Long loggedInUserId, Long fanArtId, FanArtUpdateRequest fanArtUpdateRequest) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        if (!fanArt.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        fanArt.changeFanArt(fanArtUpdateRequest.getTitle(), fanArtUpdateRequest.getContent());

        fanArt.getImageList().clear();

        if (!fanArtUpdateRequest.getImageList().isEmpty()) {
            for (MultipartFile fanArtImageFile : fanArtUpdateRequest.getImageList()) {
                String savePath = fileSaveHelper.saveFanArtImage(fanArtImageFile);

                FanArtImage fanArtImage = FanArtImage.builder()
                        .imagePath(savePath)
                        .build();

                fanArt.addImage(fanArtImage);
            }
        }
        fanArtRepository.save(fanArt);
    }

    @Override
    public void deleteFanArtComment(Long loggedInUserId, Long fanArtId, Long fanArtCommentId) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FanArtComment fanArtComment = fanArtCommentRepository.findById(fanArtCommentId)
                .orElseThrow(FanArtCommentNotFoundException::new);

        if (!fanArtComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        if (fanArtComment.getParentFanArtComment() != null) {
            fanArtComment.getParentFanArtComment().minusReplyCnt();
        }

        fanArt.minusCommentCnt(fanArtComment.getReplyCnt());

        fanArtCommentRepository.delete(fanArtComment);
    }

    @Override
    public void updateFanArtComment(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, FanArtCommentUpdateRequest fanArtCommentUpdateRequest) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FanArtComment fanArtComment = fanArtCommentRepository.findById(fanArtCommentId)
                .orElseThrow(FanArtCommentNotFoundException::new);

        if (!fanArtComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        fanArtComment.changeComment(fanArtCommentUpdateRequest.getContent());

        fanArtCommentRepository.save(fanArtComment);
    }

    @Override
    public void likeFanArt(Long loggedInUserId, Long fanArtId) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Optional<FanArtLike> existingFanArtLike = fanArtLikeRepository.findByFanArtAndUser(fanArt,user);

        if(existingFanArtLike.isPresent()){
            fanArtLikeRepository.delete(existingFanArtLike.get());
            fanArt.minusLikeCnt();
        }else{
            FanArtLike fanArtLike = FanArtLike.builder()
                    .user(user)
                    .fanArt(fanArt)
                    .build();
            fanArt.plusLikeCnt();
            fanArtLikeRepository.save(fanArtLike);

            alarmService.fanArtLikeAlarm(fanArt.getTitle(), fanArt.getUser().getId(), loggedInUserId, fanArt.getFanArtId());
        }
    }

    @Override
    public void likeFanArtComment(Long loggedInUserId, Long fanArtCommentId) {
        FanArtComment fanArtComment = fanArtCommentRepository.findById(fanArtCommentId)
                .orElseThrow(FanArtCommentNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Optional<FanArtCommentLike> existingFanArtCommentLike = fanArtCommentLikeRepository.findByFanArtCommentAndUser(fanArtComment, user);


        if (existingFanArtCommentLike.isPresent()) {
            fanArtCommentLikeRepository.delete(existingFanArtCommentLike.get());
            fanArtComment.minusLikeCnt();
        } else {
            FanArtCommentLike fanArtCommentLike = FanArtCommentLike.builder()
                    .user(user)
                    .fanArtComment(fanArtComment)
                    .build();
            fanArtComment.plusLikeCnt();
            fanArtCommentLikeRepository.save(fanArtCommentLike);
        }
    }

    @Override
    public Page<FanArtResponse> getFanArt(Long loggedInUserId, Long idolGroupId, Pageable pageable, FanArtSearchCondition condition) {
        return fanArtRepository.findFanArts(loggedInUserId, idolGroupId, pageable, condition);
    }

    @Override
    public Page<FavoriteFanArtResponse> getFavoriteFanArt(Long loggedInUserId, Pageable pageable) {
        return fanArtRepository.findFavoriteFanArt(loggedInUserId, pageable);
    }

    @Override
    public Page<MyFanArtResponse> getMyFanArt(Long loggedInUserId, Pageable pageable) {
        return fanArtRepository.findMyFanArts(loggedInUserId,pageable);

    }

    @Override
    public Page<FanArtReplyResponse> getFanArtReply(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, Pageable pageable) {
        return fanArtCommentRepository.findFanArtReply(loggedInUserId,fanArtId,fanArtCommentId,pageable);
    }

    @Override
    public Page<FanArtCommentResponse> getFanArtComment(Long loggedInUserId, Long fanArtId, Pageable pageable) {
        return fanArtCommentRepository.findFanArtComments(loggedInUserId,fanArtId,pageable);
    }

    @Override
    public FanArtDetailResponse getFanArtDetail(Long loggedInUserId, Long fanArtId) {
        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        User user = fanArt.getUser();

        List<String> fanArtImagePathList = fanArt.getImagePathList(fanArt.getImageList());

        FanArtDetailResponse response;

        if (loggedInUserId == null) {
            response = FanArtDetailResponse.builder()
                    .userId(user.getId())
                    .profileImagePath(user.getProfilePath())
                    .nickname(user.getNickname())
                    .createdAt(fanArt.getCreatedAt())
                    .lastModified(fanArt.getLastModified())
                    .likeCnt(fanArt.getLikeCnt())
                    .isLike(false)
                    .fanArtImagePathList(fanArtImagePathList)
                    .content(fanArt.getContent())
                    .commentCnt(fanArt.getCommentCnt())
                    .build();
        } else {
            User user1 = userRepository.findById(loggedInUserId)
                    .orElseThrow(UserNotFoundException::new);

            Optional<FanArtLike> fanArtLike = fanArtLikeRepository.findByFanArtAndUser(fanArt,user1);
            boolean isLike = fanArtLike.isPresent();


            response = FanArtDetailResponse.builder()
                    .userId(user.getId())
                    .profileImagePath(user.getProfilePath())
                    .nickname(user.getNickname())
                    .createdAt(fanArt.getCreatedAt())
                    .lastModified(fanArt.getLastModified())
                    .likeCnt(fanArt.getLikeCnt())
                    .isLike(isLike)
                    .fanArtImagePathList(fanArtImagePathList)
                    .content(fanArt.getContent())
                    .commentCnt(fanArt.getCommentCnt())
                    .build();
        }
        return response;
    }

    @Override
    public void addFanArtComment(Long loggedInUserId, Long fanArtId, FanArtCommentAddRequest fanArtCommentAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        FanArtComment fanArtComment = FanArtComment.builder()
                .fanArt(fanArt)
                .user(user)
                .content(fanArtCommentAddRequest.getContent())
                .build();

        fanArt.plusCommentCnt();

        fanArtCommentRepository.save(fanArtComment);
    }

    @Override
    public void addFanArtReply(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, FanArtCommentAddRequest fanArtReplyAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FanArt fanArt = fanArtRepository.findById(fanArtId)
                .orElseThrow(FanArtNotFoundException::new);

        FanArtComment parentFanArtComment = fanArtCommentRepository.findById(fanArtCommentId)
                .orElseThrow(FanArtCommentNotFoundException::new);


        if (parentFanArtComment.getFanArt().getFanArtId() == fanArtId) {
            FanArtComment fanArtComment = FanArtComment.builder()
                    .fanArt(fanArt)
                    .parentFanArtComment(parentFanArtComment)
                    .user(user)
                    .content(fanArtReplyAddRequest.getContent())
                    .build();

            if (parentFanArtComment.getParentFanArtComment() == null) {
                fanArtCommentRepository.save(fanArtComment);
                fanArt.plusCommentCnt();
                parentFanArtComment.plusReplyCnt();
            } else {
                throw new FanArtCommentNotFoundException();
            }
        } else {
            throw new FanArtCommentNotFoundException();
        }
    }


}
