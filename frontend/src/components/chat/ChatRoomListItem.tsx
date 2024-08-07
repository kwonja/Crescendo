import React from 'react';
import { ChatRoom } from '../../interface/chat';
import { BASE_URL } from '../../apis/core';
import { timeAgo } from '../../utils/TimeAgo';

interface ChatRoomItemProps {
  room: ChatRoom;
  HandleClick: (Group: ChatRoom) => void;
}
export default function ChatRoomListItem({ room, HandleClick }: ChatRoomItemProps) {
  const { opponentProfilePath, opponentNickName, lastChattingTime, lastChatting } = room;
  return (
    <div className="chatroomlistitem" onClick={() => HandleClick(room)}>
      <img src={`${BASE_URL}${opponentProfilePath}`} alt="상대방프로필" />
      <div className="content w-8/12">
        <div className="nickname">{opponentNickName}</div>
        <div className="lastchat " >{lastChatting}</div>
      </div>

      <div className="lastchattime">{timeAgo(lastChattingTime)}</div>
    </div>
  );
}
