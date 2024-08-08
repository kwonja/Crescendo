import React, { useEffect, useRef, useState } from 'react';
import Profile from '../components/mypage/Profile';
import FriendList from '../components/mypage/FriendList';
import { ReactComponent as Crown } from '../assets/images/crown.svg';
import Feed from '../components/common/Feed';
import { useAppDispatch, useAppSelector } from '../store/hooks/hook';
import Gallery from '../components/common/Gallery';
import { getMyFeedList } from '../features/feed/feedSlice';
import newjeans from '../assets/images/newjeans.png';
import { useParams } from 'react-router-dom';
import { getUserInfoAPI } from '../apis/user';
import { UserInfo } from '../interface/user';
import { getUserId } from '../apis/core';
export default function MyPage() {
  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const [userInfo, setUserInfo] = useState<UserInfo>({
    profilePath: '',
    nickname: '',
    introduction: '',
    followingNum: 0,
    followerNum: 0,
    isFollowing: false,
    favoriteImagePath: '',
  });
  const menuRef = useRef<HTMLDivElement>(null);
  const feedlist = useAppSelector(state => state.feed.myFeedList);
  const dispatch = useAppDispatch();

  const { id } = useParams<{ id: string }>();
  const numericId = id ? parseInt(id, 10) : NaN;


  const setIntroduction = (introduction: string) => {
    setUserInfo(prevUserInfo => ({
      ...prevUserInfo,
      introduction,
    }));
  };
  const setProfileImage = (profileImage: string) => {
    setUserInfo(prevUserInfo => ({
      ...prevUserInfo,
      profilePath: profileImage,
    }));
  };
  const setNickname = (nickname: string) => {
    setUserInfo(prevUserInfo => ({
      ...prevUserInfo,
      nickname,
    }));
  };
  useEffect(() => {
    dispatch(getMyFeedList(numericId));
    const getUserInfo = async()=>{
      const response = await getUserInfoAPI(numericId,getUserId());
      setUserInfo(response.data);
    } 
    getUserInfo();
  }, [dispatch,numericId]);

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
        <Profile userInfo={userInfo}
        userId={numericId}
        setIntroduction={setIntroduction}
        setNickname={setNickname}
        setProfileImage={setProfileImage}
        />
        <FriendList 
        userId={numericId}
        userInfo={userInfo}/>
      </div>
      <div className="mypage_center">
        <div className="myfavorite">
          <img
            src={newjeans}
            alt="최애"
          />
          <div className="crown">
            <Crown />
          </div>
          <div className="text">NewJeans</div>
          <div className='flex flex-row gap-2 mt-1'>
          <button className='ml-auto w-32 bg-mainColor h-8 text-white flex justify-center items-center rounded-full'>이미지 업로드</button>
          <button className='w-32 bg-subColor h-8 text-white flex justify-center items-center rounded-full'>이미지 삭제</button>
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
