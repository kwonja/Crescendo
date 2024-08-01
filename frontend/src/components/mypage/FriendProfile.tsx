import React from 'react';
import { follow } from '../../interface/follow';

interface FriendProps {
  follow: follow;
}
export default function FriendProfile({ follow }: FriendProps) {
  const { nickname, profilePath } = follow;
  return (
    <div className="friendprofile">
      <img src={profilePath} alt="유저 프로필" />
      <span>{nickname}</span>
    </div>
  );
}
