import React from 'react';
import { ReactComponent as User } from '../../assets/images/Feed/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { FeedInfo } from '../../interface/feed';
import { useAppDispatch } from '../../store/hooks/hook';
import { decrementLike, incrementLike } from '../../features/feed/feedSlice';

interface FeedProps {
  feed: FeedInfo;
}
// interface FeedInfo {
//   feedId: number;
//   userId: number;
//   profilePath: string;
//   nickname: string;
//   createdAt: string; // "2024-08-08T07:09:32.325Z",
//   lastModified: string; // "2024-08-08T07:09:32.325Z",
//   likeCnt: number;
//   imagePaths: string[];
//   content: string;
//   commentCnt: number;
//   tags: string[];
//   isLike: boolean;
// }

export default function CommunityFeed({ feed }: FeedProps) {
  const {feedId, userId, profilePath, nickname, createdAt, lastModified, likeCnt, imagePaths, content, commentCnt, tags, isLike } = feed;
  const dispatch = useAppDispatch();
  return (
    <div className="feed">
      <div className="upper">
        <User />
        <div className="userinfo">
          <span>{nickname}</span>
          <span>{createdAt}</span>
        </div>

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
              dispatch(incrementLike(feedId));
            }}
          />
        ) : (
          <FullHeart
            className="hoverup"
            onClick={() => {
              dispatch(decrementLike(feedId));
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
