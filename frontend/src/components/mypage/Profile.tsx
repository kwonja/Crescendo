import React from 'react'
import { ReactComponent as Edit } from '../../assets/images/edit.svg';
export default function Profile() {
  return (
    <div className='profile'>
        <div className='img'>
            <img src="https://cdn.topstarnews.net/news/photo/202301/15040596_1067813_363.jpg" alt="유저 프로필"/>
            <div className='edit'><Edit className='icon'/></div>
        </div>
        <div className='nickname'>
             Nickname
        </div>
        <div className='content'>
        ABCDEFGHIJKLMNOPERSTUWWXYZ
        </div>
    </div>
  )
}
