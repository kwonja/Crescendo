import React from 'react';
import { ReactComponent as Dots } from '../../assets/images/Gallery/whitedots.svg';
import { ReactComponent as User } from '../../assets/images/Gallery/whiteuser.svg';
import { ReactComponent as Heart } from '../../assets/images/Gallery/whiteheart.svg';
import { ReactComponent as Comment } from '../../assets/images/Gallery/whitecomment.svg';
import fanartImage from '../../assets/images/fanart.png';
export default function Gallery() {
  return (
    <div className="gallery">
      <img src={fanartImage} alt="팬아트그림" />

      <div className="title_box">
        <div className="type">팬아트</div>
        <div className='title'>뉴진스 민지 수채화 팬아트</div>
        <Dots style={{marginLeft : "auto"}}/>
      </div>
      <div className="gallery_userinfo">
          <User />
          <div className="gallery_profile">
              <span style={{fontSize : "20px"}}>Nickname</span>
              <span style={{fontSize : "12px"}}>2024.01.01</span>
        </div>
        <Comment className='gallery_comment'/>
        <Heart className="gallery_heart" />
      </div>
    </div>
  );
}
