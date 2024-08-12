import React from 'react';
import { ReactComponent as Dots } from '../../assets/images/Gallery/whitedots.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Gallery/whitefullheart.svg';
import { ReactComponent as Heart } from '../../assets/images/Gallery/whiteheart.svg';
import { ReactComponent as Comment } from '../../assets/images/Gallery/whitecomment.svg';
import { GalleryInfo } from '../../interface/gallery';
import { IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';

interface FanArtProps {
  fanArt: GalleryInfo;
  onClick: () => void;
}

export default function CommunityFanart({fanArt, onClick}:FanArtProps) {
  const {
          userId,
          profileImagePath,
          nickname,
          likeCnt,
          fanArtImagePathList,
          commentCnt,
          lastModified,
          title,
          isLike
        } = fanArt;

  return (
    <div className="gallery">
      <img className='gallery-img' src={IMAGE_BASE_URL+fanArtImagePathList[0]} alt="팬아트그림" />

      <div className="title_box">
        <div className="type">팬아트</div>
        <div className="title">{title}</div>
        <div className="dots_box">
          <Dots className='dots'/>
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
          {isLike?<FullHeart className="gallery_heart" />:<Heart className="gallery_heart" />}
          <div className='gallery_heart_cnt'>{likeCnt}</div>
        </div>
      </div>
    </div>
  );
}
