package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.entity.*;
import com.sokpulee.crescendo.domain.feed.repository.*;
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

import java.util.Optional;

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
    private final FeedLikeRepository feedLikeRepository;


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
    public void deleteFeed(Long feedId, Long loggedInUserId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        if (!feed.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        if (feedRepository.existsById(feedId)) {
            feedRepository.deleteById(feedId);
        } else {
            throw new FeedNotFoundException();
        }
    }

    @Override
    public void updateFeed(Long loggedInUserId, Long feedId, FeedUpdateRequest feedUpdateRequest) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        IdolGroup idolGroup = idolGroupRepository.findById(feedUpdateRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);

        if (!feed.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        feed.changeFeed(idolGroup, feedUpdateRequest.getTitle(), feedUpdateRequest.getContent());

        feed.getImageList().clear();
        if (!feedUpdateRequest.getImageList().isEmpty()) {
            for (MultipartFile feedImageFile : feedUpdateRequest.getImageList()) {
                String savePath = fileSaveHelper.saveFeedImage(feedImageFile);

                FeedImage feedImage = FeedImage.builder()
                        .imagePath(savePath)
                        .build();

                feed.addImage(feedImage);
            }
        }

        feed.getHashtagList().clear();
        if (!feedUpdateRequest.getTagList().isEmpty()) {
            for (String tag : feedUpdateRequest.getTagList()) {
                FeedHashtag feedHashtag = FeedHashtag.builder()
                        .tag(tag)
                        .build();

                feed.addHashtag(feedHashtag);
            }
        }


        feedRepository.save(feed);
    }

    @Override
    public void deleteFeedComment(Long loggedInUserId, Long feedId, Long feedCommentId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FeedComment feedComment = feedCommentRepository.findById(feedCommentId)
                .orElseThrow(FeedCommentNotFoundException::new);

        if (!feedComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        feedCommentRepository.delete(feedComment);
    }

    @Override
    public void updateFeedComment(Long loggedInUserId, Long feedId, Long feedCommentId, FeedCommentUpdateRequest feedCommentUpdateRequest) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        FeedComment feedComment = feedCommentRepository.findById(feedCommentId)
                .orElseThrow(FeedCommentNotFoundException::new);

        if (!feedComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        feedComment.changeComment(feedCommentUpdateRequest.getContent());

        feedCommentRepository.save(feedComment);
    }

    @Override
    public void likeFeed(Long loggedInUserId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Optional<FeedLike> existingFeedLike = feedLikeRepository.findByFeedAndUser(feed, user);


        if (existingFeedLike.isPresent()) {
            feedLikeRepository.delete(existingFeedLike.get());
            feed.minusLikeCnt();
        }
        else {
            FeedLike feedLike = FeedLike.builder()
                    .user(user)
                    .feed(feed)
                    .build();
            feed.plusLikeCnt();
            feedLikeRepository.save(feedLike);
        }
    }

    @Override
    public Page<FeedResponse> getFeed(Long loggedInUserId, Pageable pageable) {
        return feedRepository.findFeeds(loggedInUserId, pageable);
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
