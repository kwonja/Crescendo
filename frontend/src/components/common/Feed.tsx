import React from 'react';
import { ReactComponent as User } from '../../assets/images/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/comment.svg';
export default function Feed() {
  const tags = ['2주년', '뉴진스'];

  return (
    <div className="feed">
      <div className="upper">
        <User />
        <div className="userinfo">
          <span>Nickname</span>
          <span>2024.01.01</span>
        </div>
        <Heart className="heart" />



























        
        <Dots className="dots" />
      </div>
      <div className="text">뉴진스와 버니즈의 2주년!!</div>
      <div className="tag">
        {tags.map(tag => (
          <div>#{tag}</div>
        ))}
      </div>
      <Comment className="comment" />
    </div>
  );
}
