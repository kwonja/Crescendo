import React, { useEffect, useState } from 'react';

import FriendProfile from './FriendProfile';
import SearchInput from '../common/SearchInput';


import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getUserFollower } from '../../features/follow/followerSlice';
import { getUserFollowing } from '../../features/follow/followingSlice';


export default function FriendList() {

  const dispatch = useAppDispatch();
  const {followingList} = useAppSelector( state => state.following);
  const {followerList} = useAppSelector(state => state.follower);
  const [isSelected, setIsSelected] = useState<'follower' | 'following'>('follower');


  useEffect( ()=>{
    
    if(isSelected ==='follower')
    {
      const promise = dispatch(getUserFollower(1))
      return () => promise.abort();
    }
    else {
      const promise = dispatch(getUserFollowing(1))

      return () => promise.abort();
    }
  },[dispatch,isSelected])
  return (
    <>
      <div className="friend">
        <div className="listbar">
          <div
            className={`follow left ${isSelected === 'follower' ? 'active' : ''}`}
            onClick={() => setIsSelected('follower')}
          >
            <div>팔로우</div>
            <span>{followerList.length}</span>
          </div>
          <div
            className={`follow right ${isSelected === 'following' ? 'active' : ''}`}
            onClick={() => setIsSelected('following')}
          >
            <div>팔로워</div>
            <span>{followingList.length}</span>
          </div>
        </div>
      </div>
      <div className="list">
        <SearchInput placeholder="친구를 검색하세요" />
        <div className="profilelist">
          {isSelected ==='follower' ?

          followerList.length > 0 ? 

          followerList.map((follower,index) => (
            <FriendProfile key={index} follow={follower} />
          ))
          : <div>친구를 팔로워 해보세요!</div>
          :
          followingList.length > 0 ?
          followingList.map((following,index) => (
            <FriendProfile key={index} follow={following} />
          ))
          : <div>친구를 추가해 보세요!</div>
        }
         
            
        </div>
      </div>
    </>
  );
}
