import React from 'react';
import CommunityCard from './CommunityCard';

export default function CommunityList() {
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
    {
      idolGroupId: 7,
      name: "빅뱅",
      profile: "group-profile/newjeans.jpg"
    },
    {
      idolGroupId: 8,
      name: "르세라핌",
      profile: "group-profile/bts.jpg"
    },
    {
      idolGroupId: 9,
      name: "이달의소녀",
      profile: "group-profile/aespa.jpg"
    },
    {
      idolGroupId: 10,
      name: "(여자)아이들",
      profile: "group-profile/aespa.jpg"
    },
    {
      idolGroupId: 11,
      name: "QWER",
      profile: "group-profile/newjeans.jpg"
    },
    {
      idolGroupId: 12,
      name: "마마무",
      profile: "group-profile/bts.jpg"
    },
  ];

  // const tmpToAdd = [
  //   {
  //     idolGroupId: 13,
  //     name: "TWICE",
  //     profile: "group-profile/aespa.jpg"
  //   },
  //   {
  //     idolGroupId: 14,
  //     name: "레드벨벳",
  //     profile: "group-profile/aespa.jpg"
  //   },
  //   {
  //     idolGroupId: 15,
  //     name: "IVE",
  //     profile: "group-profile/newjeans.jpg"
  //   },
  //   {
  //     idolGroupId: 16,
  //     name: "프로미스나인",
  //     profile: "group-profile/bts.jpg"
  //   },
  // ];



  return <div className='communitymain_contents' >
    <div className='communitymain_list'>
      {
        tmpList.map((community)=>(<CommunityCard idolGroupId={community.idolGroupId} 
          name={community.name} profile={community.profile} key={community.idolGroupId} />))
      }
    </div>
  </div>;
}