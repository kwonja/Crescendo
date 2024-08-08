import { useParams } from 'react-router-dom';
import { ReactComponent as FullStar } from '../assets/images/CommunityDetail/fullstar.svg';
import { ReactComponent as Star } from '../assets/images/CommunityDetail/star.svg';
import React, { useEffect, useRef, useState } from 'react';
import SearchInput from '../components/common/SearchInput';
import Dropdown from "../components/common/Dropdown";
import CommunityFeedList from '../components/community/CommunityFeedList';
import GalleryList from '../components/common/GalleryList';
import FeedForm from '../components/community/PostFeed';
import GalleryForm from '../components/community/PostGallery';
import { ReactComponent as WriteButton } from '../assets/images/write.svg';
import FeedDetailModal from '../components/community/FeedDetailModal'; // 피드 상세 모달 임포트
import { getCommunityDetailAPI, toggleFavoriteAPI } from '../apis/community';
import { useAppDispatch, useAppSelector } from '../store/hooks/hook';
import { CommunityDetailInfo } from '../interface/communityList';
import { searchFeed, setFilterCondition, setSortCondition } from '../features/feed/communityFeedSlice';



export default function CommunityDetail() {
  const params = useParams();
  if (params.idolGroupId === undefined || !/^[1-9]\d*$/.test(params.idolGroupId)) {
    throw new Error('invalid parameter');
  }
  const idolGroupId: number = Number(params.idolGroupId);

  const initialDetail: CommunityDetailInfo = {
    idolGroupId: 0,
    name: '',
    peopleNum: 0,
    introduction: '',
    profile: '',
    banner: '',
    favoriteCnt: 0,
    isFavorite: false,
  };

  const [ communityDetail, setCommunityDetail ] = useState<CommunityDetailInfo>(initialDetail)
  const { isLoggedIn } = useAppSelector(state => state.auth);
  const [isSelected, setIsSelected] = useState<'feed' | 'gallery'>('feed');
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const menuRef = useRef<HTMLDivElement>(null);
  const { filterCondition, sortCondition } = useAppSelector(state => state.communityFeed);
  const [searchOption, setSearchOption] = useState<string>('검색');
  const [searchKeyword, setSearchKeyword] = useState<string>('');
  const dispatch = useAppDispatch();

  const [show, setShow] = useState(false);
  const [activeTab, setActiveTab] = useState('feed');
  const [showDetail, setShowDetail] = useState(false);
  const [selectedFeedId, setSelectedFeedId] = useState<number | null>(null);

  // 데이터 가져오기, 초기세팅
  useEffect(()=> {
    const getCommunityDetail = async () => {
      const response = await getCommunityDetailAPI(idolGroupId);
      setCommunityDetail(response);
    }
    getCommunityDetail();

  }, [idolGroupId])

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
    setSearchOption('검색');
    setSearchKeyword('');
  }, [isSelected]);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleCloseDetail = () => {
    setShowDetail(false);
  };

  const handleShowDetail = () => {
    setSelectedFeedId(1); // 테스트용 피드 ID를 1로 설정
    setShowDetail(true);
  } 

  const clickStar = async () => {
    try {
      await toggleFavoriteAPI(idolGroupId);
      setCommunityDetail(prev => ({
        ...prev,
        isFavorite: !prev.isFavorite,
        favoriteCnt: prev.isFavorite?prev.favoriteCnt-1:prev.favoriteCnt+1
      }));
    } 
    catch {
      console.error("즐겨찾기 토글 실패");
    }
  }

  return (
    <div className="communitydetail">
      <div className="banner">
        <img
          className="banner_img"
          src={communityDetail.banner}
          alt={communityDetail.name}
        ></img>
        <div className="banner_name">{communityDetail.name}</div>
        <div className="banner_favoritenum">{`${communityDetail.favoriteCnt} Favorites`}</div>
        {isLoggedIn && (
          <div className="banner_starbox">
            {communityDetail.isFavorite ? (
              <FullStar className="hoverup banner_star" onClick={()=>clickStar()} />
            ) : (
              <Star className="hoverup banner_star" onClick={()=>clickStar()} />
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
              selected={filterCondition}
              options={["전체", "팔로우만"]}
              onSelect={(selected)=>dispatch(setFilterCondition(selected))}
            />
          </div>
          <div className="search">
            <div className = "sort menu">
              <Dropdown
                className="text"
                selected={sortCondition}
                options={["최신순", "좋아요순"]}
                onSelect={(selected)=>dispatch(setSortCondition(selected))}
                iconPosition="left"
              />
            </div>
            <div className = "search menu">
              <Dropdown
                className="text"
                selected={searchOption}
                options={["내용", "작성자"]}
                onSelect={(selected)=>setSearchOption(selected)}
                iconPosition="left"
              />
            </div>
            <SearchInput 
              placeholder="여기에 입력하세요" 
              value={searchKeyword}
              onChange={(event)=>{
                setSearchKeyword(event.target.value);
              }}
              onSearch={()=>dispatch(searchFeed({searchOption, searchKeyword}))}
              ></SearchInput>
          </div>
        </div>
        {isSelected === 'feed' && <CommunityFeedList idolGroupId={idolGroupId} />}
        {isSelected === 'gallery' && <GalleryList />}
      </div>

      {isLoggedIn && <WriteButton className="write-button" onClick={handleShow} />}

      {/* 테스트용 피드 상세 모달 열기 버튼 */}
      <button onClick={handleShowDetail} style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '10px 20px', background: '#007BFF', color: '#fff', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
        테스트용 피드 상세 모달 열기
      </button>

      {selectedFeedId && (
        <FeedDetailModal
          show={showDetail}
          onClose={handleCloseDetail}
          feedId={selectedFeedId}
        />
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
