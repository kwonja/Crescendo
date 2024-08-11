import React, { useCallback, useEffect, useState } from 'react'
import { getUserInfoAPI, modifyIntroductionAPI, modifyNicknameAPI } from '../../apis/user';
import { getUserId } from '../../apis/core';
import { followAPI } from '../../apis/follow';
import { UserInfo } from '../../interface/user';
import { useParams } from 'react-router-dom';
import Profile from './Profile';
import FriendList from './FriendList';

export default function LeftInfo() {
    const { id } = useParams<{ id: string }>();
    const numericId = id ? parseInt(id, 10) : NaN;
    const [userInfo, setUserInfo] = useState<UserInfo>({
        profilePath: '',
        nickname: '',
        introduction: '',
        followingNum: 0,
        followerNum: 0,
        isFollowing: false,
        favoriteImagePath: '',
      });
      
    const handleFollow = useCallback( async()=>{
        await followAPI(numericId);
        setUserInfo( prev => {return {...prev, isFollowing : !prev.isFollowing}})
      },[numericId]);

      const handleUpdate = async(nickname : string, introduction : string)=>{
    
        if (userInfo.introduction !== introduction) {
          await modifyIntroductionAPI(numericId, introduction);
    
        }
        if (userInfo.nickname !== nickname) {
          await modifyNicknameAPI(numericId, nickname);
    
        }
        setUserInfo(prev =>  { return{...prev, nickname ,introduction}});
      }

      useEffect( ()=>{
        const getUserInfo = async () => {
            try {
              const response = await getUserInfoAPI(numericId, getUserId());
              if(response.data.introduction === null)
              {
                response.data.introduction = '';
              }
              setUserInfo({...response.data});
            } catch (err: unknown) {
              // console.log(err);
            }
          };
          getUserInfo();
      },[numericId])
      
  return (
    <>
    <button onClick={()=>{}}>버튼버튼</button>
    <Profile
          userInfo={userInfo}
          handleUpdate={handleUpdate}
          userId={numericId}
          handleFollow={handleFollow}
        />
    <FriendList userId={numericId} userInfo={userInfo} />
    </>
  )
}
