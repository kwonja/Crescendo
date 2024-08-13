import React from 'react';
import { ReactComponent as Dots } from '../../assets/images/Gallery/whitedots.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Gallery/whitefullheart.svg';
import { ReactComponent as Heart } from '../../assets/images/Gallery/whiteheart.svg';
import { ReactComponent as Comment } from '../../assets/images/Gallery/whitecomment.svg';
import { MyGoodsInfo } from '../../interface/gallery';
import { IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleGoodsLike } from '../../features/communityDetail/communityDetailSlice';

interface GoodsProps {
  goods: MyGoodsInfo;
}

export default function MyGoods({goods}:GoodsProps) {
  const {
          goodsId,
          userId,
          profileImagePath,
          nickname,
          likeCnt,
          goodsImagePathList,
          commentCnt,
          lastModified,
          title,
          isLike
        } = goods;
  
  const dispatch = useAppDispatch();
      

  return (
    <div className="gallery">
      <img className='gallery-img' src={IMAGE_BASE_URL+goodsImagePathList[0]} alt="굿즈그림" />

      <div className="title_box">
        <div className="type">굿즈</div>
        <div className="title">{title}</div>
        <div className="dots_box">
          <Dots className='dots hoverup'/>
        </div>
      </div>
      <div className="gallery_info">
        <div className="gallery_profile">
          <UserProfile
            userId={userId}
            userNickname={nickname}
            date={lastModified}
            userProfilePath={profileImagePath ? IMAGE_BASE_URL + profileImagePath : null}
          />
        </div>
        <div className="gallery_comment_box">
          <Comment className="gallery_comment" />
          <div className='gallery_comment_cnt'>{commentCnt}</div>
        </div>
        <div className = "gallery_heart_box">
          {isLike?<FullHeart className="gallery_heart hoverup" onClick={()=>dispatch(toggleGoodsLike(goodsId))} />
                  :<Heart className="gallery_heart hoverup" onClick={()=>dispatch(toggleGoodsLike(goodsId))} />}
          <div className='gallery_heart_cnt'>{likeCnt}</div>
        </div>
      </div>
    </div>
  );
}
