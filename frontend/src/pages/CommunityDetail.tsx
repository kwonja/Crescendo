import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../store/store';
import { ReactComponent as FullStar } from '../assets/images/fullstar.svg';
import { ReactComponent as Star } from '../assets/images/star.svg';
import React, { useEffect, useRef, useState } from 'react';
import SearchInput from '../components/common/SearchInput';
import Dropdown from "../components/common/Dropdown";
import FeedList from '../components/common/FeedList';
import GalleryList from '../components/common/GalleryList';
import FeedForm from '../components/community/PostFeed';
import GalleryForm from '../components/community/PostGallery';
import { ReactComponent as WriteButton } from '../assets/images/write.svg';
import '../scss/page/_communitydetail.scss';

type communityDetailInfoType = {
  idolGroupId: number;
  name: string;
  peopleNum: number;
  introduction: string;
  profile: string;
  banner: string;
  isFavorite: boolean;
};

export default function CommunityDetail() {
  const { idolGroupId } = useParams();
  const isLoggedIn = useSelector((state: RootState) => state.auth.isLoggedIn);

  // 임시 데이터
  const communityDetailInfo: communityDetailInfoType = {
    idolGroupId: Number(idolGroupId),
    name: 'NewJeans',
    peopleNum: 5,
    introduction: '뉴진스입니다.',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    banner: 'https://i.ibb.co/3s0NMP0/126.jpg',
    isFavorite: false,
  };
  const favoriteNum = 0;
  const isLogin = true;
  // 임시 데이터

  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [isFavorite, setisFavorite] = useState<boolean>(false);
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);
  const [filterOption, setFilterOption] = useState<string>('필터');
  const [sortOption, setSortOption] = useState<string>('정렬');
  const [searchOption, setSearchOption] = useState<string>('검색');

  const [show, setShow] = useState(false);
  const [activeTab, setActiveTab] = useState('feed');

  function clickStar() {
    setisFavorite(prev => !prev);
    // 이후 rest-api서버로 전송
  }

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
    setFilterOption('필터');
    setSortOption('정렬');
    setSearchOption('검색');
  }, [isSelected]);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  return (
    <div className="communitydetail">
      <div className="banner">
        <img
          className="banner_img"
          src={communityDetailInfo.banner}
          alt={communityDetailInfo.name}
        ></img>
        <div className="banner_name">{communityDetailInfo.name}</div>
        <div className="banner_favoritenum">{`${favoriteNum} Favorites`}</div>
        {isLogin && (
          <div className="banner_star">
            {isFavorite ? (
              <FullStar className="hoverup" onClick={clickStar} />
            ) : (
              <Star className="hoverup" onClick={clickStar} />
            )}
          </div>
        )}
      </div>
      <div className="communitydetail_container">
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
        <div className="conditionbar">
            <div className="filter menu">
              <Dropdown
                className="text"
                selected={filterOption}
                options={["전체", "팔로우만"]}
                onSelect={(selected)=>setFilterOption(selected)}
              />
            </div>
            <div className="search">
              <div className = "sort menu">
                <Dropdown
                  className="text"
                  selected={sortOption}
                  options={["가나다순", "최신순", "좋아요순"]}
                  onSelect={(selected)=>setSortOption(selected)}
                  iconPosition="left"
                />
              </div>
              <div className = "search menu">
                <Dropdown
                  className="text"
                  selected={searchOption}
                  options={["제목", "작성자"]}
                  onSelect={(selected)=>setSearchOption(selected)}
                  iconPosition="left"
                />
              </div>
              <SearchInput placeholder="여기에 입력하세요"></SearchInput>
            </div>
        </div>
        {isSelected === 'feed' && <FeedList />}
        {isSelected === 'gallery' && <GalleryList />}
      </div>

      {isLoggedIn && <WriteButton className="write-button" onClick={handleShow} />}

      {show && (
        <div className="modal">
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-header-title">
                <h2>글작성</h2>
              </div>
              <div className="tabs">
                <button
                  className={`tab ${activeTab === 'feed' ? 'active' : ''}`}
                  onClick={() => setActiveTab('feed')}
                >
                  피드
                </button>
                <button
                  className={`tab ${activeTab === 'gallery' ? 'active' : ''}`}
                  onClick={() => setActiveTab('gallery')}
                >
                  갤러리
                </button>
              </div>
              <span className="close" onClick={handleClose}>
                &times;
              </span>
            </div>
            <div className="modal-body">
              {activeTab === 'feed' ? <FeedForm /> : <GalleryForm />}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
