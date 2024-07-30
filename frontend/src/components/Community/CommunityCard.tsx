import React from 'react';


type communityInfoProps = {
  idolGroupId: number; // 아이돌 그룹 ID
  name: string; // 아이돌 그룹 명
  profile: string; // 아이돌 그룹 프로필 사진 경로
};


export default function CommunityCard({idolGroupId,name,profile}:communityInfoProps) {
  return <div className='communitymain_card'>
    <img src='data:image/webp;base64,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' alt={name}></img>
    <div className='communitymain_card_name'>{name}</div>
  </div>;
}
