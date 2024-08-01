import React, { useEffect } from 'react'
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getUserFollowing } from '../../features/follow/followingSlice';
import FriendProfile from './FriendProfile';

export default function FollowingList() {
    const dispatch = useAppDispatch();
    const { followingList,error } = useAppSelector(state => state.following);
    useEffect( ()=>{
        const promise = dispatch(getUserFollowing(1));
        return () => promise.abort();
    },[dispatch])

    if(error)
    {
        return <div>데이터를 가져오는중 에러가 발생했습니다. 잠시만 기다려주세요</div>
    }

    return (
        <div className="profilelist">
            { followingList.length > 0 ? (
              followingList.map((following, index) => <FriendProfile key={index} follow={following} />)
            ) : (
              <div>친구를 팔로잉 해보세요!</div>
            )}
      </div>
  )
}
