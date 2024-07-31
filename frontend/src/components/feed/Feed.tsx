import React, { useState } from 'react';
import { ReactComponent as User } from '../../assets/images/Feed/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { FeedData } from '../../interface/feed';

interface FeedProps {
  feed: FeedData;
}
export default function Feed({ feed }: FeedProps) {
  const [like, setLike] = useState<number>(99);
  const [comment, setComment] = useState<number>(99);
  const [isLiked, setisLiked] = useState<boolean>(false);

  return (
    <div className="feed">
      <div className="upper">
        <User />
        <div className="userinfo">
          <span>{feed.nickname}</span>
          <span>{feed.createdAt}</span>
        </div>

        <Dots className="dots hoverup" />
      </div>
      <div className="text">{feed.content}</div>
      <div className="tag">
        {feed.tagList.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="feed_heart_box">
        {like}
        {!isLiked ? (
          <Heart
            className="hoverup"
            onClick={() => {
              setLike(prev => prev + 1);
              setisLiked(true);
            }}
          />
        ) : (
          <FullHeart
            className="hoverup"
            onClick={() => {
              setLike(prev => prev - 1);
              setisLiked(false);
            }}
          />
        )}
      </div>
      <div className="feed_comment_box">
        {' '}
        {comment}
        <Comment
          className="hoverup"
          onClick={() => {
            setComment(prev => prev + 1);
          }}
        />
      </div>
    </div>
  );
}
