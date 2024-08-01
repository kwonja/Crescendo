import { useParams } from "react-router-dom"
import { ReactComponent as FullStar } from "../assets/images/fullstar.svg"
import { ReactComponent as Star } from "../assets/images/star.svg"
import React, { useEffect, useRef, useState } from 'react';

type communityDetailInfoType = {
  idolGroupId: number,
  name: string,
  peopleNum: number,
  introduction: string,
  profile: string,
  banner: string,
  isFavorite: boolean
}

export default function CommunityDetail() {
  const {idolGroupId} = useParams();
  
  // 임시 데이터
  const communityDetailInfo:communityDetailInfoType = {
    idolGroupId: Number(idolGroupId),
    name: "NewJeans", 
    peopleNum: 5,
    introduction: "뉴진스입니다.",
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    banner: 'https://i.ibb.co/3s0NMP0/126.jpg',
    isFavorite: false
  }
  const favoriteNum = 0;
  const isLogin = true;

  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const menuElement = menuRef.current;
    if (menuElement) {
      const activeLink = menuElement.querySelector('.active') as HTMLElement;
      if (activeLink) {
        const { offsetLeft, offsetWidth } = activeLink;
        setIndicatorStyle({
          left: offsetLeft + (offsetWidth - 160) / 2 + 'px', // Center the indicator
        });
      }
    }
  }, [isSelected]);


  return <div className="communitydetail">
      <div className="banner">
        <img className="banner_img" src={communityDetailInfo.banner} alt={communityDetailInfo.name}></img>
        <div className="banner_name">{communityDetailInfo.name}</div>
        <div className="banner_favoritenum">{`${favoriteNum} Favorites`}</div>
        {isLogin && <div className="banner_star" onClick={()=>{}}>{communityDetailInfo.isFavorite?<FullStar/>:<Star/>}</div>}
      </div>
      <div className="category" ref={menuRef}>
          <div
            className={`item ${isSelected === 'feed' ? 'active' : ''}`}
            onClick={() => setIsSelected('feed')}
          >
            피드
          </div>
          <div
            className={`item ${isSelected === 'gallery' ? 'active' : ''}`}
            onClick={() => setIsSelected('gallery')}
          >
            갤러리
          </div>
          <div className="indicator" style={indicatorStyle}></div>
        </div>
    </div>
}
