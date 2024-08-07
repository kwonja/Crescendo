import React from 'react';
import { user } from '../../interface/user';

interface UserProps {
  user: user;
}

export default function FriendProfile({ user }: UserProps) {
  const { nickname, userProfilePath } = user;
  return (
    <div className="friendprofile">
      <img src={`${process.env.REACT_APP_IMAGE_BASEURL}${userProfilePath}`} alt="유저 프로필" />
      <span>{nickname}</span>
    </div>
  );
}
