import React from 'react';
import { ReactComponent as Dots } from '../../assets/images/dots.svg';
import { ReactComponent as User } from '../../assets/images/reduser.svg';
import { ReactComponent as Heart } from '../../assets/images/heart.svg';
import fanartImage from '../../assets/images/fanart.png';
export default function Gallery() {
  return (
    <div className="gallery">
      <img src={fanartImage} alt="팬아트그림" />

      <div className="title">
        <div className="title">팬아트</div>
        <div>뉴진스 민지 수채화 팬아트</div>
        <Dots />
      </div>
      <div className="userinfo">
        <User />
        <div className="profile">
          <span>Nickname</span>
          <span>2024.01.01</span>
        </div>
        <Heart className="heart" />
        <Dots className="dots" />
      </div>
    </div>
  );
}
