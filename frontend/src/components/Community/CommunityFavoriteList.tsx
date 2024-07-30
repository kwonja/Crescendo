import React, { useEffect, useState } from 'react';
import CommunityCard from './CommunityCard';
import Button from '../common/Button';
import axios from 'axios';


export default function CommunityFavoriteList() {
  // 타입 정의
  type communityInfo = {
    idolGroupId: number; // 아이돌 그룹 ID
    name: string; // 아이돌 그룹 명
    profile: string; // 아이돌 그룹 프로필 사진 경로
  };

  // 임시 데이터
  const tmpList:communityInfo[] = [ // 현재 페이지의 데이터 목록 (커뮤니티 목록)
    {
      idolGroupId: 1,
      name: "NewJeans", // 아이돌 그룹 명
      profile: "group-profile/newjeans.jpg" // 아이돌 그룹 프로필 사진 경로 - 대충 막 정함
    },
    {
      idolGroupId: 2,
      name: "BTS", // 아이돌 그룹 명
      profile: "group-profile/bts.jpg" // 아이돌 그룹 프로필 사진 경로 - 대충 막 정함
    },
    {
      idolGroupId: 3,
      name: "Aespa", // 아이돌 그룹 명
      profile: "group-profile/aespa.jpg" // 아이돌 그룹 프로필 사진 경로 - 대충 막 정함
    },
    {
      idolGroupId: 4,
      name: "Black Pink", // 아이돌 그룹 명
      profile: "group-profile/aespa.jpg" // 아이돌 그룹 프로필 사진 경로 - 대충 막 정함
    },
  ];
  
  // 상수 또는 변수(상태) 초기화
  const size:number = 4;
  const apiPath:string = "";
  let maxPage:number = 1;

  const [page, setPage] = useState<number>(1);
  const [communityList, setCommunityList] = useState<communityInfo[]>([])

  // 리스트 불러오기
  useEffect(()=>{
    axios.get(apiPath, {
      params: {
        page,
        size
      },
    })
    .then((response)=>{
      setCommunityList(response.data.content);
      maxPage = response.data.pageable.totalElements;
    })
    .catch(()=>{
      setCommunityList(tmpList);
    })
  },[page])
  
  function onIncrease() {
    if (page < maxPage) setPage(page+1);
    else setPage(1);
    console.log(page);
  }

  function onDecrease() {
    if (page > 1 ) setPage(page-1);
    else setPage(maxPage);
    console.log(page);
  }

  return <div className='communitymain_favoritelist_container'>
    <Button className='prev-btn' onClick={onDecrease}>&lt;</Button>
    <div className='communitymain_favoritelist_contents'>
      {communityList.map((community)=>(<CommunityCard idolGroupId={community.idolGroupId} name={community.name} profile={community.profile} key={community.idolGroupId} />))}
    </div>
    <Button className='next-btn' onClick={onIncrease}>&gt;</Button>
  </div>;
}