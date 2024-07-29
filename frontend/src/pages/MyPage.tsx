import React from 'react'
import Profile from '../components/mypage/Profile'
import FriendList from '../components/mypage/FriendList'

export default function MyPage() {
  return (
    <div className='mypage'>
    <div className='mypage_left'>
        <Profile/>
        <FriendList/>
    </div>
    <div className='mypage_center'></div>
    </div>
  )
}
