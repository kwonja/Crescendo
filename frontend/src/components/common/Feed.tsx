import React, { useState } from 'react';
import { ReactComponent as User } from '../../assets/images/Feed/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
export default function Feed() {
  const [like,setLike]= useState<number>(99);
  const [comment,setComment]= useState<number>(99);
  const [isLiked,setisLiked]=useState<boolean>(false);
  const tags = ['2주년', '뉴진스'];

  return (
    <div className="feed">
      <div className="upper">
        <User />
        <div className="userinfo">
          <span>Nickname</span>
          <span>2024.01.01</span>
        </div>
       
        <Dots className="dots" />
      </div>
      <div className="text">뉴진스와 버니즈의 2주년!!</div>
      <div className="tag">
        {tags.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="heart">{like} 
        {
          !isLiked ? <Heart onClick={()=>{ setLike(prev=>prev+1); setisLiked(true)}}/> : 
          <FullHeart
          onClick={()=>{ setLike(prev=>prev-1);setisLiked(false)}}
          />
        }
        </div>
      <div className="comment"> {comment} 
        <Comment onClick={()=>{setComment(prev=>prev+1)}}/>  
        </div>
    </div>
  );
}
