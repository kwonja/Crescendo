package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.entity.Feed;
import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import com.sokpulee.crescendo.domain.feed.entity.FeedHashtag;
import com.sokpulee.crescendo.domain.feed.entity.FeedImage;
import com.sokpulee.crescendo.domain.feed.repository.FeedCommentRepository;
import com.sokpulee.crescendo.domain.feed.repository.FeedHashtagRepository;
import com.sokpulee.crescendo.domain.feed.repository.FeedImageRepository;
import com.sokpulee.crescendo.domain.feed.repository.FeedRepository;
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

@Transactional
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FeedHashtagRepository feedHashtagRepository;
    private final FeedImageRepository feedImageRepository;
    private final IdolGroupRepository idolGroupRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final FileSaveHelper fileSaveHelper;


    @Override
    public void addFeed(Long loggedInUserId, FeedAddRequest feedAddRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);


        IdolGroup idolGroup = idolGroupRepository.findById(feedAddRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);


        Feed feed = Feed.builder()
                .idolGroup(idolGroup)
                .user(user)
                .title(feedAddRequest.getTitle())
                .content(feedAddRequest.getContent())
                .build();

        if (!feedAddRequest.getTagList().isEmpty()) {
            for (String tag : feedAddRequest.getTagList()) {
                FeedHashtag feedHashtag = FeedHashtag.builder()
                        .tag(tag)
                        .build();

                feed.addHashtag(feedHashtag);
            }
        }

        if (!feedAddRequest.getImageList().isEmpty()) {
            for (MultipartFile feedImageFile : feedAddRequest.getImageList()) {
                String savePath = fileSaveHelper.saveFeedImage(feedImageFile);

                FeedImage feedImage = FeedImage.builder()
                        .imagePath(savePath)
                        .build();

                feed.addImage(feedImage);
            }
        }

        feedRepository.save(feed);
    }

    @Override
    public void addFeedComment(Long loggedInUserId, Long feedId, FeedCommentAddRequest feedCommentAddRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        FeedComment feedComment = FeedComment.builder()
                .feed(feed)
                .user(user)
                .content(feedCommentAddRequest.getContent())
                .build();

        feedCommentRepository.save(feedComment);
    }

    @Override
    public void addFeedReply(Long loggedInUserId, Long feedId, Long feedCommentId, FeedCommentAddRequest feedReplyAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        FeedComment parentFeedComment = feedCommentRepository.findById(feedCommentId)
                .orElseThrow(FeedCommentNotFoundException::new);

        FeedComment feedComment = FeedComment.builder()
                .feed(feed)
                .parentFeedComment(parentFeedComment)
                .user(user)
                .content(feedReplyAddRequest.getContent())
                .build();

        if (parentFeedComment.getParentFeedComment() == null) {
            feedCommentRepository.save(feedComment);
        } else {
            throw new FeedCommentNotFoundException();
        }

    }
}
