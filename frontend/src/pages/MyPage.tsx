import React, { useEffect, useRef, useState } from 'react';
import { ReactComponent as Crown } from '../assets/images/crown.svg';
import Feed from '../components/common/Feed';
import { useAppDispatch, useAppSelector } from '../store/hooks/hook';
import Gallery from '../components/common/Gallery';
import { getMyFeedList } from '../features/mypage/myFeedSlice';
import newjeans from '../assets/images/newjeans.png';
import { useParams } from 'react-router-dom';

import LeftInfo from '../components/mypage/LeftInfo';
export default function MyPage() {
  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);
  const feedlist = useAppSelector(state => state.feed.myFeedList);
  const dispatch = useAppDispatch();

  const { id } = useParams<{ id: string }>();
  const numericId = id ? parseInt(id, 10) : NaN;

  useEffect(() => {
    dispatch(getMyFeedList(numericId));
  }, [dispatch, numericId]);

  const updateIndicator = () => {
    const menuElement = menuRef.current;
    if (menuElement) {
      const activeLink = menuElement.querySelector('.active') as HTMLElement;
      if (activeLink) {
        const { offsetLeft, offsetWidth } = activeLink;
        setIndicatorStyle({
          left: offsetLeft + (offsetWidth - 160) / 2 + 'px', // Center the indicator
          display: 'block',
        });
      } else {
        setIndicatorStyle({ display: 'none' });
      }
    }
  };

  useEffect(() => {
    updateIndicator();
  }, [isSelected]);
  useEffect(() => {
    window.addEventListener('resize', updateIndicator);
    return () => {
      window.removeEventListener('resize', updateIndicator);
    };
  }, []);
  return (
    <div className="mypage">
      <div className="mypage_left">
        <LeftInfo />
      </div>
      <div className="mypage_center">
        <div className="myfavorite">
          <img src={newjeans} alt="최애" />
          <div className="crown">
            <Crown />
          </div>
          <div className="text">NewJeans</div>
          <div className="flex flex-row gap-2 mt-1">
            <button className="ml-auto w-32 bg-mainColor h-8 text-white flex justify-center items-center rounded-full">
              이미지 업로드
            </button>
            <button className="w-32 bg-subColor h-8 text-white flex justify-center items-center rounded-full">
              이미지 삭제
            </button>
          </div>
        </div>

        <div className="category">
          <div className="w-3/4 mx-auto space-between flex flex-row" ref={menuRef}>
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
          </div>
          <div className="indicator" style={indicatorStyle}></div>
        </div>

        {isSelected === 'feed' && (
          <div className="">
            {feedlist.map(feed => (
              <Feed key={feed.feedId} feed={feed} />
            ))}
          </div>
        )}

        {isSelected === 'gallery' && (
          <div className="mypage_gallery">
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
