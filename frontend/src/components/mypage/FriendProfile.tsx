import React from 'react';
import { user } from '../../interface/user';
import { IMAGE_BASE_URL } from '../../apis/core';

interface UserProps {
  user: user;
}

export default function FriendProfile({ user }: UserProps) {
  const { nickname, userProfilePath } = user;

  return (
    <div className="friendprofile">
      <img src={`${IMAGE_BASE_URL}${userProfilePath}`} alt="유저 프로필" />
      <span>{nickname}</span>
    </div>
  );
}
