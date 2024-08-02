import React from 'react';
import { communityInfo } from '../../interface/communityList';


export default function CommunityCard({ idolGroupId, name, profile }: communityInfo) {
  return (
    <div className="communitycard">
      <img className="communitycard_img" src={profile} alt={name}></img>
      <div className="communitycard_name">{name}</div>
    </div>
  );
}
