import React from 'react';
import { ReactComponent as User } from '../../assets/images/Feed/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { FeedData } from '../../interface/feed';
import { useAppDispatch } from '../../store/hooks/hook';
import { decrementLike, incrementLike } from '../../features/feed/feedSlice';
import { ChatDateTranfer } from '../../utils/ChatDateTranfer';

interface FeedProps {
  feed: FeedData;
}
export default function Feed({ feed }: FeedProps) {
  const { nickname, createdAt, likeCnt, content, commentCnt, tagList, isLike, feedId } = feed;
  const dispatch = useAppDispatch();
  return (
    <div className="feed">
      <div className="upper">
        <User />
        <div className="userinfo">
          <span>{nickname}</span>
          <span>{ChatDateTranfer(createdAt)}</span>
        </div>

        <Dots className="dots hoverup" />
      </div>
      <div className="text">{content}</div>
      <div className="tag">
        {tagList.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="feed_heart_box">
        {likeCnt}
        {!isLike ? (
          <Heart
            className="hoverup w-8 h-8"
            onClick={() => {
              dispatch(incrementLike(feedId));
            }}
          />
        ) : (
          <FullHeart
            className="hoverup w-8 h-8"
            onClick={() => {
              dispatch(decrementLike(feedId));
            }}
          />
        )}
      </div>
      <div className="feed_comment_box">
        {' '}
        {commentCnt}
        <Comment className="hoverup w-8 h-8" />
      </div>
    </div>
  );
}
