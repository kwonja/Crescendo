import React from 'react';
import ChatRoomListItem from './ChatRoomListItem';

export default function ChatLayout() {
  return (
    <div className="chatlayout">
      <div className="title">채팅목록</div>


      <div className="chatroomlist">
        <ChatRoomListItem />
        <ChatRoomListItem />
        <ChatRoomListItem />
        <ChatRoomListItem />
      </div>

      
    </div>
  );
}
