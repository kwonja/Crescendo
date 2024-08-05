import React from 'react';
import { useAppSelector } from '../../store/hooks/hook';

import FriendProfile from './FriendProfile';


export default function FollowingList() {
  const { followingList, error,status } = useAppSelector(state => state.following);
  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (error === 'failed') {
    return <div>데이터를 가져오는 중 에러가 발생했습니다: {error}</div>;
  }

  return (
    <div className="profilelist">
      {followingList.length > 0 ? (
        followingList.map((following, index) => <FriendProfile key={index} user={following} />)
      ) : (
        <div>친구를 팔로잉 해보세요!</div>
      )}
    </div>
  );
}
