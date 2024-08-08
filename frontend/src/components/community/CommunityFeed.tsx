import React from 'react';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { FeedInfo } from '../../interface/feed';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFeedLike } from '../../features/feed/communityFeedSlice';
import UserProfile from '../common/UserProfile';
import { IMAGE_BASE_URL } from '../../apis/core';

interface FeedProps {
  feed: FeedInfo;
}

export default function CommunityFeed({ feed }: FeedProps) {
  const {feedId, userId, profilePath, nickname, lastModified, likeCnt, imagePaths, content, commentCnt, tags, isLike } = feed;
  const dispatch = useAppDispatch();

  return (
    <div className="feed">
      <div className="upper">
        <UserProfile userId={userId} userNickname={nickname} date={lastModified} userProfilePath={profilePath?IMAGE_BASE_URL+profilePath:null} />

        <Dots className="dots hoverup" />
      </div>
      <div className="text">{content}</div>
      <div className="tag">
        {tags.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="feed_heart_box">
        {likeCnt}
        {!isLike ? (
          <Heart
            className="hoverup"
            onClick={() => {
              dispatch(toggleFeedLike(feedId));
            }}
          />
        ) : (
          <FullHeart
            className="hoverup"
            onClick={() => {
              dispatch(toggleFeedLike(feedId));
            }}
          />
        )}
      </div>
      <div className="feed_comment_box">
        {' '}
        {commentCnt}
        <Comment className="hoverup" />
      </div>
    </div>
  );
}
