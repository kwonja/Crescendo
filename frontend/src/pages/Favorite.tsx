import BestPhotoSlide from "../components/favorite/BestPhotoSlide";
import Dropdown from "../components/common/Dropdown";
import { useEffect, useState } from "react";
import FavoriteRankList from "../components/favorite/FavoriteRankList";
import {idolGroupInfo, idolInfo} from '../interface/favorite'
import { getidolGroupListAPI, getIdolListAPI } from "../apis/favorite";
import { useAppDispatch } from "../store/hooks/hook";
import { getFavoriteRankList, resetPage, setIdolId, setSortByVotes } from "../features/favorite/favoriteSlice";

export default function Favorite() {

  const [idolGroupOption, setIdolGroupOption] = useState<string>('그룹');
  const [idolGroupList, setIdolGroupList] = useState<idolGroupInfo[]>([]);
  const [idolOption, setIdolOption] = useState<string>('멤버');
  const [idolList, setIdolList] = useState<idolInfo[]>([]);
  const [sortOption, setSortOption] = useState<string>('정렬');
  const dispatch = useAppDispatch();

  //그룹 리스트 가져오기
  useEffect(()=> {
    const getIdolGroupList = async () => {
      const response = await getidolGroupListAPI();
      setIdolGroupList(response);
    }
    getIdolGroupList();
  }, [])

  //멤버 리스트 가져오기
  useEffect(()=>{
    if (idolGroupOption === '그룹') {
      setIdolList([]);
      return;
    }
    setIdolOption('멤버');
    const selectedGroupId = idolGroupList.find((group)=> group.groupName===idolGroupOption)?.groupId;
    if (!selectedGroupId) throw new Error('not found idol group');
    const getIdolList = async (groupId:number) => {
      const response = await getIdolListAPI(groupId);
      setIdolList(response);
    }
    getIdolList(selectedGroupId);
  
  }, [idolGroupOption, idolGroupList])

  // 최애 자랑 리스트 가져오기
  useEffect(()=>{
    dispatch(resetPage());
    const selectedIdolId = idolList.find((idol)=> idol.idolName===idolOption)?.idolId;
    if (selectedIdolId) {
      dispatch(setIdolId(selectedIdolId));
      dispatch(getFavoriteRankList());
    }
  }, [idolOption])


  return <div className="favorite">
      <div className="bestphotos_container">
        <BestPhotoSlide/>
      </div>
      <div className="favorite_container">
        <div className="favorite_title">
          최애 자랑 갤러리
        </div>
        <div className="conditionbar">
          <div className="filter">
            <div className="menu">
              <Dropdown
                className="group text"
                selected={idolGroupOption}
                options={idolGroupList.map(group=>group.groupName)}
                onSelect={(selected)=>setIdolGroupOption(selected)}
              />
            </div>
            <div className = "menu">
              <Dropdown
                className="member text"
                selected={idolOption}
                options={idolList.map(idol=>idol.idolName)}
                onSelect={(selected)=>setIdolOption(selected)}
              />
            </div>
          </div>
          <div className = "sort menu">
            <Dropdown
              className="sort text"
              selected={sortOption}
              options={["최신순", "좋아요순"]}
              onSelect={(selected)=>{
                setSortOption(selected);
                dispatch(setSortByVotes(selected))
              }}
              iconPosition="left"
            />
          </div>
        </div>
        <FavoriteRankList />
      </div>
    </div>

}
