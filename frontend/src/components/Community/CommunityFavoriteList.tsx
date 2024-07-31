import React, { useEffect, useState } from 'react';
import CommunityCard from './CommunityCard';
import Button from '../common/Button';
// import axios from 'axios';
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
  // const API_PATH:string = "";
  const SIZE_PER_PAGE = 4; // 한번에 몇개의 그룹이 보일지
  const MOVE_STEP = 4; // 화살표 클릭시 몇개의 그룹을 넘길지

  const [idx, setIdx] = useState<number>(-1); // 로딩 전 -1
  const [communityList, setCommunityList] = useState<communityInfo[]>([])
  const [showList, setShowList] = useState<communityInfo[]>([])

  // 리스트 불러오기
  useEffect(() =>{
    // axios.get(API_PATH)
    // .then((response)=>{
    //   setCommunityList(response.data.content);
    // })
    // .catch(()=>{})

    // 테스트용 코드
    setCommunityList(tmpList);
    setIdx(0);  
  },[]);

  useEffect(()=>{
    if (idx === -1) return; // 로딩 전 로딩안함
    let tmp = [...communityList];
    tmp = tmp.slice(idx, idx+SIZE_PER_PAGE);
    setShowList(tmp);
    // console.log(showList);
  }, [idx]);

  
  function onIncrease() {
    if (idx+SIZE_PER_PAGE+MOVE_STEP < communityList.length-1) setIdx((prev)=>prev+MOVE_STEP);
    else {
      setIdx(communityList.length-SIZE_PER_PAGE);
    }
    // console.log(idx);
  }

  function onDecrease() {
    if (idx-MOVE_STEP > 0) setIdx((prev)=>prev-MOVE_STEP);
    else {
      setIdx(0);
    }
    // console.log(idx);
  }

  return <div className='communitymain_favoritelist_container'>
    {<Button className={idx===0?'hidden ':''+'square empty'} onClick={onDecrease}><LeftBtn/></Button>}
    <div className='communitymain_favoritelist_contents'>
      {showList.map((community)=>(<CommunityCard idolGroupId={community.idolGroupId} name={community.name} profile={community.profile} key={community.idolGroupId} />))}
    </div>
    {<Button className={idx>=communityList.length-4?'hidden ':''+'square empty'} onClick={onIncrease}><RightBtn/></Button>}
  </div>;
}