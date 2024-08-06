import React from 'react';
import { user } from '../../interface/user';

interface UserProps {
  user: user;
}

export default function FriendProfile({ user }: UserProps) {
  const { nickname, profilePath } = user;
  return (
    <div className="friendprofile">
      <img src={`${process.env.REACT_APP_IMAGE_BASEURL}${profilePath}`} alt="유저 프로필" />
      <span>{nickname}</span>
    </div>
  );
}
