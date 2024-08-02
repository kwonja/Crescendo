import React from 'react';
import { ChatRoom } from '../../interface/chat';
import { BASE_URL } from '../../apis/core';
import { timeAgo } from '../../utils/TimeAgo';

interface ChatRoomItemProps {
  room: ChatRoom;
  HandleClick: (GroupId: number) => void;
}
export default function ChatRoomListItem({ room, HandleClick }: ChatRoomItemProps) {
  const { opponentProfilePath, opponentNickname, lastChattingTime, lastChatting, dmGroupId } = room;
  return (
    <div className="chatroomlistitem" onClick={() => HandleClick(dmGroupId)}>
      <img src={`${BASE_URL}${opponentProfilePath}`} alt="상대방프로필" />
      <div className="content">
        <div>{opponentNickname}</div>
        <div>{lastChatting}</div>
      </div>

      <div className="lastchattime">{timeAgo(lastChattingTime)}</div>
    </div>
  );
}
