import React, { useEffect, useRef, useState } from 'react';
import Profile from '../components/mypage/Profile';
import FriendList from '../components/mypage/FriendList';
import { ReactComponent as Crown } from '../assets/images/crown.svg';
// import Feed from '../components/feed/Feed';
import Gallery from '../components/common/Gallery';
// import { useAppSelector } from '../store/hooks/hook';
export default function MyPage() {
  const [isSelected, setIsSelected] = useState<string>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);
  // const feedlist = useAppSelector(state => state.feed);
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

  return (
    <div className="mypage">
      <div className="mypage_left">
        <Profile />
        <FriendList />
      </div>
      <div className="mypage_center">
        <div className="myfavorite">
          <img
            src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7Gh48vV6C_LMRFTzwuoR8lD4y_K20Uf09nQ&s"
            alt="최애"
          />
          <div className="crown">
            <Crown />
          </div>
          <div className="text">NewJeans</div>
        </div>

        <div className="category" ref={menuRef}>
          <div
            className={`item ${isSelected === 'feed' ? 'active' : ''}`}
            onClick={() => setIsSelected('feed')}
          >
            내 피드
          </div>
          <div
            className={`item ${isSelected === 'gallery' ? 'active' : ''}`}
            onClick={() => setIsSelected('gallery')}
          >
            내 갤러리
          </div>
          <div className="indicator" style={indicatorStyle}></div>
        </div>

        {isSelected === 'feed' && (
          <div className="">
            {/* <Feed />
            <Feed />
            <Feed />
            <Feed /> */}
          </div>
        )}

        {isSelected === 'feed' && (
          <div className="">
            {/* <Feed />
            <Feed />
            <Feed />
            <Feed /> */}
          </div>
        )}

        {isSelected === 'gallery' && (
          <div className="mypage_gallery">
            <Gallery />
            <Gallery />
            <Gallery />
            <Gallery />
            <Gallery />
            <Gallery />
            <Gallery />
            <Gallery />
          </div>
        )}
      </div>
    </div>
  );
}
