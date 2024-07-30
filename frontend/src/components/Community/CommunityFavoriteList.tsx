import React, { useEffect, useState } from 'react';
import CommunityCard from './CommunityCard';
import Button from '../common/Button';
import axios from 'axios';
import { ReactComponent as RightBtn } from '../../assets/images/right.svg';
import { ReactComponent as LeftBtn } from '../../assets/images/left.svg';

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
      name: "NewJeans",
      profile: "group-profile/newjeans.jpg" // 대충 막 정함
    },
    {
      idolGroupId: 2,
      name: "BTS",
      profile: "group-profile/bts.jpg"
    },
    {
      idolGroupId: 3,
      name: "Aespa",
      profile: "group-profile/aespa.jpg"
    },
    {
      idolGroupId: 4,
      name: "Black Pink",
      profile: "group-profile/aespa.jpg"
    },
  ];

  const tmpNext:communityInfo[] = [
    {
      idolGroupId: 5,
      name: "소녀시대",
      profile: "group-profile/newjeans.jpg"
    },
    {
      idolGroupId: 6,
      name: "오마이걸",
      profile: "group-profile/bts.jpg"
    },
  ];
  
  // 상수 또는 변수(상태) 초기화
  const size:number = 4;
  const apiPath:string = "";

  const [page, setPage] = useState<number>(1);
  const [maxPage, setMaxPage] = useState<number>(1);
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
      setMaxPage(response.data.pageable.totalElements);
    })
    .catch(()=>{})

    // 테스트용 코드
    if (page === 1) setCommunityList(tmpList);
    else setCommunityList(tmpNext);
    setMaxPage(2);
  },[page])
  
  function onIncrease() {
    if (page < maxPage) setPage((prev)=>prev+1);
    else setPage(1);
    console.log(page);
  }

  function onDecrease() {
    if (page > 1 ) setPage((prev)=>prev-1);
    else setPage(maxPage);
    console.log(page);
  }

  return <div className='communitymain_favoritelist_container'>
    <Button className='square empty' onClick={onDecrease}><LeftBtn/></Button>
    <div className='communitymain_favoritelist_contents'>
      {communityList.map((community)=>(<CommunityCard idolGroupId={community.idolGroupId} name={community.name} profile={community.profile} key={community.idolGroupId} />))}
    </div>
    <Button className='square empty' onClick={onIncrease}><RightBtn/></Button>
  </div>;
}