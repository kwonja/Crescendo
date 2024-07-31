package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentAddRequest;
import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtImage;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtCommentRepository;
import com.sokpulee.crescendo.domain.fanart.repository.FanArtRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FanArtServiceImpl implements FanArtService {

    private final UserRepository userRepository;

    private final IdolGroupRepository idolGroupRepository;

    private final FileSaveHelper fileSaveHelper;

    private final FanArtRepository fanArtRepository;
    private final FanArtCommentRepository fanArtCommentRepository;

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

        FanArtComment fanArtComment = FanArtComment.builder()
                .fanArt(fanArt)
                .parentFanArtComment(parentFanArtComment)
                .user(user)
                .content(fanArtReplyAddRequest.getContent())
                .build();

        if (parentFanArtComment.getParentFanArtComment() == null) {
            fanArtCommentRepository.save(fanArtComment);
        } else {
            throw new FanArtCommentNotFoundException();
        }
    }
}
