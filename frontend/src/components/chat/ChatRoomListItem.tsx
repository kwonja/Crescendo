import React from 'react';
import { ChatRoom } from '../../interface/chat';
import { timeAgo } from '../../utils/TimeAgo';
import { ReactComponent as User } from '../../assets/images/Chat/user.svg';
import { IMAGE_BASE_URL } from '../../apis/core';

interface ChatRoomItemProps {
  room: ChatRoom;
  HandleClick: (Group: ChatRoom) => void;
}

export default function ChatRoomListItem({ room, HandleClick }: ChatRoomItemProps) {
  const { opponentProfilePath, opponentNickName, lastChattingTime, lastChatting } = room;
  return (
    <div className="chatroomlistitem" onClick={() => HandleClick(room)}>
      {opponentProfilePath ? (
        <div className='m-1.5 h-12 w-12'><img src={`${IMAGE_BASE_URL}${opponentProfilePath}`} alt="프로필" className='w-full h-full rounded-full'/></div>
      ) : (
        <div className='m-1.5 w-12 h-12'><User className='w-full h-full'/></div>
      )}
      <div className="content w-8/12">
        <div className="nickname">{opponentNickName}</div>
        <div className="lastchat ">{lastChatting}</div>
      </div>

      <div className="lastchattime">{timeAgo(lastChattingTime)}</div>
    </div>
  );
}
