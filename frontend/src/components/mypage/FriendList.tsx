import React, { useState } from 'react'
import { ReactComponent as Search } from '../../assets/images/search.svg';
import FriendProfile from './FriendProfile';
export default function FriendList() {
  const [isSelected,setIsSelected] =useState<string>('left');
  return (
    <>
    <div className='friend'>
          <div className='listbar'>
            <div className={`follow left ${isSelected === 'left' ? 'active' :''}`} 
              onClick={ ()=>setIsSelected('left')}
            >
            <div>팔로우</div>
            <span>1000</span>
            </div>
          <div className={`follow right ${isSelected === 'right' ? 'active' :''}`} 
          onClick={ ()=>setIsSelected('right')}
          >
          <div>팔로워</div>
          <span>500</span>
          </div>
        </div>
    </div>
    <div className='list'>

      <div className="search-container">
        <span><input type="text" placeholder="친구를 검색하세요"/>
        <div className='search-icon'><Search/></div>
        </span>
      </div>

      <div className='profilelist'>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
        <FriendProfile/>
      </div>
    </div>
    </>
    
  )
}
