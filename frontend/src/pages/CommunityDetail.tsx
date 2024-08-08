import { useParams } from 'react-router-dom';
import { ReactComponent as FullStar } from '../assets/images/CommunityDetail/fullstar.svg';
import { ReactComponent as Star } from '../assets/images/CommunityDetail/star.svg';
import React, { useEffect, useRef, useState } from 'react';
import SearchInput from '../components/common/SearchInput';
import Dropdown from '../components/common/Dropdown';
import FeedList from '../components/common/FeedList';
import GalleryList from '../components/common/GalleryList';
import FeedForm from '../components/community/PostFeed';
import GalleryForm from '../components/community/PostGallery';
import { ReactComponent as WriteButton } from '../assets/images/write.svg';
import FeedDetailModal from '../components/community/FeedDetailModal';
import '../scss/page/_communitydetail.scss';
import { getCommunityDetailAPI, toggleFavoriteAPI } from '../apis/community';
import { useAppSelector } from '../store/hooks/hook';
import { communityDetailInfo } from '../interface/communityList';

export default function CommunityDetail() {
  const params = useParams();
  if (params.idolGroupId === undefined || !/^[1-9]\d*$/.test(params.idolGroupId)) {
    throw new Error('invalid parameter');
  }
  const idolGroupId: number = Number(params.idolGroupId);

  const initialDetail: communityDetailInfo = {
    idolGroupId: 0,
    name: '',
    peopleNum: 0,
    introduction: '',
    profile: '',
    banner: '',
    isFavorite: false,
  };

  // 임시데이터 나중에 api 수정되면 수정함
  const favoriteNum = 0;

  const [communityDetail, setCommunityDetail] = useState<communityDetailInfo>(initialDetail);
  const { isLoggedIn } = useAppSelector(state => state.auth);
  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);
  const [filterOption, setFilterOption] = useState<string>('필터');
  const [sortOption, setSortOption] = useState<string>('정렬');
  const [searchOption, setSearchOption] = useState<string>('검색');

  const [show, setShow] = useState(false);
  const [activeTab, setActiveTab] = useState('feed');
  const [showDetail, setShowDetail] = useState(false);
  const [selectedFeedId, setSelectedFeedId] = useState<number | null>(null);

  // 데이터 가져오기, 초기세팅
  useEffect(() => {
    const getCommunityDetail = async () => {
      const response = await getCommunityDetailAPI(idolGroupId);
      setCommunityDetail(response);
    };
    getCommunityDetail();
  }, [idolGroupId]);

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

  const handleCloseDetail = () => {
    setShowDetail(false);
  };

  const handleShowDetail = () => {
    setSelectedFeedId(1); // 테스트용 피드 ID를 1로 설정
    setShowDetail(true);
  };

  const clickStar = async () => {
    try {
      await toggleFavoriteAPI(idolGroupId);
      setCommunityDetail(prev => ({
        ...prev,
        isFavorite: !prev.isFavorite,
      }));
    } catch {
      console.error('즐겨찾기 토글 실패');
    }
  };

  return (
    <div className="communitydetail">
      <div className="banner">
        <img className="banner_img" src={communityDetail.banner} alt={communityDetail.name}></img>
        <div className="banner_name">{communityDetail.name}</div>
        <div className="banner_favoritenum">{`${favoriteNum} Favorites`}</div>
        {isLoggedIn && (
          <div className="banner_starbox">
            {communityDetail.isFavorite ? (
              <FullStar className="hoverup banner_star" onClick={() => clickStar()} />
            ) : (
              <Star className="hoverup banner_star" onClick={() => clickStar()} />
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
              options={['전체', '팔로우만']}
              onSelect={selected => setFilterOption(selected)}
            />
          </div>
          <div className="search">
            <div className="sort menu">
              <Dropdown
                className="text"
                selected={sortOption}
                options={['가나다순', '최신순', '좋아요순']}
                onSelect={selected => setSortOption(selected)}
                iconPosition="left"
              />
            </div>
            <div className="search menu">
              <Dropdown
                className="text"
                selected={searchOption}
                options={['제목', '작성자']}
                onSelect={selected => setSearchOption(selected)}
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

      {/* 테스트용 피드 상세 모달 열기 버튼 */}
      <button
        onClick={handleShowDetail}
        style={{
          position: 'fixed',
          bottom: '20px',
          left: '20px',
          padding: '10px 20px',
          background: '#007BFF',
          color: '#fff',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer',
        }}
      >
        테스트용 피드 상세 모달 열기
      </button>

      {selectedFeedId && (
        <FeedDetailModal show={showDetail} onClose={handleCloseDetail} feedId={selectedFeedId} />
      )}

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
