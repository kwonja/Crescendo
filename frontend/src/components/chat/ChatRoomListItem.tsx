import React from 'react'

export default function Chatroom() {
  return (
    <div className='chatroomlistitem'>
        <img src="/" alt='유저프로필'/>
        <div className='content'>
            <div>닉네임</div>
            <div>마지막 채팅입니다~</div>
        </div>

        <div className='lastchattime'>35분전</div>

    </div>
  )
}
