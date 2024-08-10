import React from 'react';
import BestPhotoSlide from '../components/favorite/BestPhotoSlide';
import Dropdown from '../components/common/Dropdown';
import { useEffect, useState } from 'react';
import FavoriteRankList from '../components/favorite/FavoriteRankList';
import { IdolGroupInfo, IdolInfo } from '../interface/favorite';
import { getidolGroupListAPI, getIdolListAPI } from '../apis/favorite';
import { useAppDispatch, useAppSelector } from '../store/hooks/hook';
import {
  getFavoriteRankList,
  resetPage,
  setIdolId,
  setSortByVotes,
} from '../features/favorite/favoriteSlice';
import { ReactComponent as WriteButton } from '../assets/images/write.svg';
import FavoriteRankPostModal from '../components/favorite/FavoriteRankPostModal';

export default function Favorite() {
  const [idolGroupOption, setIdolGroupOption] = useState<string>('그룹');
  const [idolGroupList, setIdolGroupList] = useState<IdolGroupInfo[]>([]);
  const [idolOption, setIdolOption] = useState<string>('멤버');
  const [idolList, setIdolList] = useState<IdolInfo[]>([]);
  const [sortOption, setSortOption] = useState<string>('정렬');
  const [showWriteModal, setShowWriteModal] = useState<boolean>(false);
  const { isLoggedIn } = useAppSelector(state=>state.auth);
  const dispatch = useAppDispatch();

  //그룹 리스트 가져오기
  useEffect(() => {
    const getIdolGroupList = async () => {
      const response = await getidolGroupListAPI();
      setIdolGroupList(response);
    };
    getIdolGroupList();
    return () => {
      setIdolGroupOption('그룹');
    };
  }, []);

  //멤버 리스트 가져오기
  useEffect(() => {
    if (idolGroupOption === '그룹') {
      setIdolList([]);
      return;
    }
    const selectedGroupId = idolGroupList.find(
      group => group.groupName === idolGroupOption,
    )?.groupId;
    if (!selectedGroupId) throw new Error('not found idol group');
    const getIdolList = async (groupId: number) => {
      const response = await getIdolListAPI(groupId);
      setIdolList(response);
    };
    getIdolList(selectedGroupId);
    return () => {
      setIdolOption('멤버');
    };
  }, [idolGroupOption, idolGroupList]);

  // 최애 자랑 리스트 가져오기
  useEffect(() => {
    const selectedIdolId = idolList.find(idol => idol.idolName === idolOption)?.idolId;
    if (selectedIdolId) {
      dispatch(setIdolId(selectedIdolId));
      dispatch(getFavoriteRankList());
    }
    return () => {
      dispatch(resetPage());
    };
  }, [idolOption, dispatch, idolList]);

  // 작성 모달 관련 함수
  

  return (
    <div className="favorite">
      <div className="bestphotos_container">
        <BestPhotoSlide />
      </div>
      <div className="favorite_container">
        <div className="favorite_title">최애 자랑 갤러리</div>
        <div className="conditionbar">
          <div className="filter">
            <div className="menu">
              <Dropdown
                className="group text"
                selected={idolGroupOption}
                options={idolGroupList.map(group => group.groupName)}
                onSelect={selected => setIdolGroupOption(selected)}
              />
            </div>
            <div className="menu">
              <Dropdown
                className="member text"
                selected={idolOption}
                options={idolList.map(idol => idol.idolName)}
                onSelect={selected => setIdolOption(selected)}
              />
            </div>
          </div>
          <div className="sort menu">
            <Dropdown
              className="sort text"
              selected={sortOption}
              options={['최신순', '좋아요순']}
              onSelect={selected => {
                setSortOption(selected);
                dispatch(setSortByVotes(selected));
              }}
              iconPosition="left"
            />
          </div>
        </div>
        <FavoriteRankList />
      </div>
      {isLoggedIn && <WriteButton className="write-button" onClick={()=>setShowWriteModal(true)} />}
      {showWriteModal && <FavoriteRankPostModal onClose={()=>setShowWriteModal(false)} idolGroupList={idolGroupList} />}
    </div>
  );
}
