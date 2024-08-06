package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentUpdateRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtUpdateRequest;
import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtImage;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtLike;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtCommentRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtLikeRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtRepository;
import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        IdolGroup idolGroup = idolGroupRepository.findById(fanArtUpdateRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);

        if (!fanArt.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        fanArt.changeFanArt(idolGroup, fanArtUpdateRequest.getTitle(), fanArtUpdateRequest.getContent());

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
    public void likeFeed(Long loggedInUserId, Long fanArtId) {
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
        }
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
