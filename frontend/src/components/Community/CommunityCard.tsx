import React from 'react';


type communityInfoProps = {
  idolGroupId: number; // 아이돌 그룹 ID
  name: string; // 아이돌 그룹 명
  profile: string; // 아이돌 그룹 프로필 사진 경로
};


export default function CommunityCard({idolGroupId,name,profile}:communityInfoProps) {
  return <div className='communitymain_card'>
    <img src={profile} alt={name}></img>
    <div className='communitymain_card_name'>{name}</div>
  </div>;
}
