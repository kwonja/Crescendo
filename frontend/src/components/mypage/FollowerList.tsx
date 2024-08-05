import React  from 'react';
import {  useAppSelector } from '../../store/hooks/hook';
import FriendProfile from './FriendProfile';

export default function Followerlist() {
  const { followerList, error,status } = useAppSelector(state => state.follower);

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (error === 'failed') {
    return <div>데이터를 가져오는 중 에러가 발생했습니다: {error}</div>;
  }

  return (
    <div className="profilelist">
      {followerList.length > 0 ? (
        followerList.map((follower, index) => <FriendProfile key={index} user={follower} />)
      ) : (
        <div>친구를 추가해 보세요!</div>
      )}
    </div>
  );
}
