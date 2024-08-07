package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.response.*;
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

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final IdolGroupRepository idolGroupRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final FileSaveHelper fileSaveHelper;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentLikeRepository feedCommentLikeRepository;


    @Override
    public void addFeed(Long loggedInUserId, FeedAddRequest feedAddRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);


        IdolGroup idolGroup = idolGroupRepository.findById(feedAddRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);


        Feed feed = Feed.builder()
                .idolGroup(idolGroup)
                .user(user)
                .content(feedAddRequest.getContent())
                .likeCnt(0)
                .commentCnt(0)
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


        if (!feed.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        feed.changeFeed(feedUpdateRequest.getContent());

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

        if (feedComment.getParentFeedComment() != null) {
            feedComment.getParentFeedComment().minusReplyCnt();
        }

        feed.minusCommentCnt(feedComment.getReplyCnt());

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
        } else {
            FeedLike feedLike = FeedLike.builder()
                    .user(user)
                    .feed(feed)
                    .build();
            feed.plusLikeCnt();
            feedLikeRepository.save(feedLike);
        }
    }

    @Override
    public Page<FeedResponse> getFeed(Long loggedInUserId, Long idolGroupId, Pageable pageable) {
        return feedRepository.findFeeds(loggedInUserId, idolGroupId, pageable);
    }

    @Override
    public FeedDetailResponse getFeedDetail(Long loggedInUserId, Long feedId) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        User user = feed.getUser();

        List<String> feedImagePathList = feed.getImagePathList(feed.getImageList());
        List<String> tagList = feed.getTagList(feed.getHashtagList());

        FeedDetailResponse response;

        if (loggedInUserId == null) {
            response = FeedDetailResponse.builder()
                    .userId(user.getId())
                    .profileImagePath(user.getProfilePath())
                    .nickname(user.getNickname())
                    .createdAt(feed.getCreatedAt())
                    .lastModified(feed.getLastModified())
                    .likeCnt(feed.getLikeCnt())
                    .isLike(false)
                    .feedImagePathList(feedImagePathList)
                    .content(feed.getContent())
                    .commentCnt(feed.getCommentCnt())
                    .tagList(tagList)
                    .build();
        } else {
            User user1 = userRepository.findById(loggedInUserId)
                    .orElseThrow(UserNotFoundException::new);

            Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndUser(feed, user1);
            boolean isLike = feedLike.isPresent();


            response = FeedDetailResponse.builder()
                    .userId(user.getId())
                    .profileImagePath(user.getProfilePath())
                    .nickname(user.getNickname())
                    .createdAt(feed.getCreatedAt())
                    .lastModified(feed.getLastModified())
                    .likeCnt(feed.getLikeCnt())
                    .isLike(isLike)
                    .feedImagePathList(feedImagePathList)
                    .content(feed.getContent())
                    .commentCnt(feed.getCommentCnt())
                    .tagList(tagList)
                    .build();
        }
        return response;
    }

    @Override
    public Page<FeedCommentResponse> getFeedComment(Long loggedInUserId, Long feedId, Pageable pageable) {
        return feedCommentRepository.findFeedComments(loggedInUserId, feedId, pageable);
    }

    @Override
    public Page<FeedReplyResponse> getFeedReply(Long loggedInUserId, Long feedId, Long feedCommentId, Pageable pageable) {
        return feedCommentRepository.findFeedReply(loggedInUserId, feedId, feedCommentId, pageable);
    }

    @Override
    public Page<FavoriteFeedResponse> getFavoriteFeed(Long loggedInUserId, Pageable pageable) {
        return feedRepository.findFavoriteFeeds(loggedInUserId, pageable);
    }

    @Override
    public Page<MyFeedResponse> getMyFeed(Long loggedInUserId, Pageable pageable) {
        return feedRepository.findMyFeeds(loggedInUserId,pageable);
    }

    @Override
    public void likeFeedComment(Long loggedInUserId, Long feedCommentId) {
        FeedComment feedComment = feedCommentRepository.findById(feedCommentId)
                .orElseThrow(FeedCommentNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Optional<FeedCommentLike> existingFeedCommentLike = feedCommentLikeRepository.findByFeedCommentAndUser(feedComment, user);


        if (existingFeedCommentLike.isPresent()) {
            feedCommentLikeRepository.delete(existingFeedCommentLike.get());
            feedComment.minusLikeCnt();
        } else {
            FeedCommentLike feedCommentLike = FeedCommentLike.builder()
                    .user(user)
                    .feedComment(feedComment)
                    .build();
            feedComment.plusLikeCnt();
            feedCommentLikeRepository.save(feedCommentLike);
        }
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

        feed.plusCommentCnt();

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


        if (parentFeedComment.getFeed().getFeedId() == feedId) {
            FeedComment feedComment = FeedComment.builder()
                    .feed(feed)
                    .parentFeedComment(parentFeedComment)
                    .user(user)
                    .content(feedReplyAddRequest.getContent())
                    .build();

            if (parentFeedComment.getParentFeedComment() == null) {
                feedCommentRepository.save(feedComment);
                feed.plusCommentCnt();
                parentFeedComment.plusReplyCnt();
            } else {
                throw new FeedCommentNotFoundException();
            }
        } else {
            throw new FeedCommentNotFoundException();
        }
    }


}
