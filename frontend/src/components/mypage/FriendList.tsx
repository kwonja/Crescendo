import React, { useEffect, useState } from 'react';
import SearchInput from '../common/SearchInput';

import { useAppDispatch } from '../../store/hooks/hook';
import { getUserFollower } from '../../features/mypage/followerSlice';
import { getUserFollowing } from '../../features/mypage/followingSlice';
import FollowingList from './FollowingList';
import Followerlist from './FollowerList';
import { UserInfo } from '../../interface/user';


interface FrinedsProps {
  userInfo : UserInfo;
  userId : number;
}
type ModeState = 'follower' | 'following' | '';
export default function FriendList( {userInfo,userId} : FrinedsProps) {
  const dispatch = useAppDispatch();
  const [isSelected, setIsSelected] = useState<ModeState>('');

  const handleModeClick = (mode: ModeState) => {
    setIsSelected( prevMode => ( prevMode === mode ? '' : mode));
  };

  useEffect(() => {
    if (isSelected === 'follower') {
      const promise = dispatch(getUserFollower(userId));
      return () => promise.abort();
    } else {
      const promise = dispatch(getUserFollowing(userId));

      return () => promise.abort();
    }
  }, [dispatch, isSelected,userId]);


  
  return (
    <>
      <div className="friend">
        <div className="listbar">
          <div
            className={`follow left ${isSelected === 'follower' ? 'active' : ''}`}
            onClick={() => handleModeClick('follower')}
          >
            <span>{userInfo.followerNum}</span>
            <div>팔로우</div>
            
          </div>
          <div
            className={`follow right ${isSelected === 'following' ? 'active' : ''}`}
            onClick={() => handleModeClick('following')}
          >
            <span>{userInfo.followingNum}</span>
            <div>팔로잉</div>
            
          </div>
        </div>
      </div>
      {
        (isSelected !== '')  && <div className="list">
        <SearchInput placeholder="친구를 검색하세요" />
        {isSelected === 'follower' ? <Followerlist /> : <FollowingList />}
      </div>
      }
      
    </>
  );
}
