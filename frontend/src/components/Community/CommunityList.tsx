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
  const tmpList: communityInfo[] = [
    // 현재 페이지의 데이터 목록 (커뮤니티 목록)
    {
      idolGroupId: 1,
      name: 'NewJeans',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 2,
      name: 'BTS',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 3,
      name: 'Aespa',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 4,
      name: 'Black Pink',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 5,
      name: '소녀시대',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 6,
      name: '오마이걸',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 7,
      name: '빅뱅',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 8,
      name: '르세라핌',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 9,
      name: '이달의소녀',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 10,
      name: '(여자)아이들',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 11,
      name: 'QWER',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 12,
      name: '마마무',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
  ];

  return (
    <div className="communitylist_contents">
      {tmpList.map(community => (
        <CommunityCard
          idolGroupId={community.idolGroupId}
          name={community.name}
          profile={community.profile}
          key={community.idolGroupId}
        />
      ))}
    </div>
  );
}
