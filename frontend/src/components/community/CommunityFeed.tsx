import React, { useState } from 'react';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { ReactComponent as RightBtn } from '../../assets/images/right.svg';
import { ReactComponent as LeftBtn } from '../../assets/images/left.svg';
import { FeedInfo } from '../../interface/feed';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFeedLike } from '../../features/communityDetail/communityDetailSlice';
import UserProfile from '../common/UserProfile';
import { IMAGE_BASE_URL } from '../../apis/core';
import Button from '../common/Button';

interface FeedProps {
  feed: FeedInfo;
  onClick: () => void;
}

export default function CommunityFeed({ feed, onClick }: FeedProps) {
  const {
    feedId,
    userId,
    profilePath,
    nickname,
    lastModified,
    likeCnt,
    imagePaths,
    content,
    commentCnt,
    tags,
    isLike,
  } = feed;
  const dispatch = useAppDispatch();
  const [imgIdx, setImgIdx] = useState<number>(0);

  const handleClick = () => {
    onClick();
  };

  return (
    <div className="feed" onClick={handleClick}>
      <div className="upper">
        <UserProfile
          userId={userId}
          userNickname={nickname}
          date={lastModified}
          userProfilePath={profilePath ? IMAGE_BASE_URL + profilePath : null}
        />
        <Dots className="dots hoverup" onClick={(e) => e.stopPropagation()}/>
      </div>
      {imagePaths.length > 0 && (
        <div className="feed_image_box">
          <div className="slider">
            <div onClick={(e) => e.stopPropagation()}>
            <Button
              className={`square empty ${imgIdx <= 0 ? 'hidden ' : ''}`}
              onClick={() => {
                setImgIdx(prev => prev - 1);
              }}
            >
              <LeftBtn />
            </Button>
            </div>
            <div className="main_img_container">
              {imgIdx > 0 && (
                <img
                  className="prev_img"
                  src={IMAGE_BASE_URL + imagePaths[imgIdx - 1]}
                  alt="이미지 없음"
                />
              )}
              <img
                className="main_img"
                src={IMAGE_BASE_URL + imagePaths[imgIdx]}
                alt="이미지 없음"
              />
              {imgIdx < imagePaths.length - 1 && (
                <img
                  className="next_img"
                  src={IMAGE_BASE_URL + imagePaths[imgIdx + 1]}
                  alt="이미지 없음"
                />
              )}
              <div className="image-counter">
                {imgIdx + 1}/{imagePaths.length}
              </div>
            </div>
            <div onClick={(e) => e.stopPropagation()}>
            <Button
              className={`square empty ${imgIdx >= imagePaths.length - 1 ? 'hidden ' : ''}`}
              onClick={() => setImgIdx(prev => prev + 1)}
            >
              <RightBtn />
            </Button>
            </div>
          </div>
          <div className="pagination-dots" onClick={(e) => e.stopPropagation()}>
            {imagePaths.map((_, idx) => (
              <div
                key={idx}
                className={`pagination-dot ${idx === imgIdx ? 'active' : ''}`}
                onClick={() => setImgIdx(idx)}
              ></div>
            ))}
          </div>
        </div>
      )}
      <div className="text">{content}</div>
      <div className="tag">
        {tags.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="feed_heart_box" onClick={(e) => e.stopPropagation()}>
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
      <div className="feed_comment_box" onClick={(e) => e.stopPropagation()}>
        {' '}
        {commentCnt}
        <Comment className="hoverup" />
      </div>
    </div>
  );
}
