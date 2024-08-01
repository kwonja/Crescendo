import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getUserFollower } from '../../features/follow/followerSlice';
import FriendProfile from './FriendProfile';

export default function Followerlist() {
  const dispatch = useAppDispatch();
  const { followerList, error } = useAppSelector(state => state.follower);
  useEffect(() => {
    const promise = dispatch(getUserFollower(1));
    return () => promise.abort();
  }, [dispatch]);

  if (error) {
    return <div>데이터를 가져오는중 에러가 발생했습니다. 잠시만 기다려주세요</div>;
  }

  return (
    <div className="profilelist">
      {followerList.length > 0 ? (
        followerList.map((follower, index) => <FriendProfile key={index} follow={follower} />)
      ) : (
        <div>친구를 추가해 보세요!</div>
      )}
    </div>
  );
}
