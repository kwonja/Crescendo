import React from 'react';
import { ChatRoom } from '../../interface/chat';
import { timeAgo } from '../../utils/TimeAgo';
import { ReactComponent as User } from '../../assets/images/user.svg';

interface ChatRoomItemProps {
  room: ChatRoom;
  HandleClick: (Group: ChatRoom) => void;
}

export default function ChatRoomListItem({ room, HandleClick }: ChatRoomItemProps) {
  const { opponentProfilePath, opponentNickName, lastChattingTime, lastChatting } = room;
  return (
    <div className="chatroomlistitem" onClick={() => HandleClick(room)}>
      {opponentProfilePath ? (
        <img
          src={`${process.env.REACT_APP_IMAGE_BASEURL}${opponentProfilePath}`}
          alt="상대방프로필"
        />
      ) : (
        <User className="user" />
      )}
      <div className="content w-8/12">
        <div className="nickname">{opponentNickName}</div>
        <div className="lastchat ">{lastChatting}</div>
      </div>

      <div className="lastchattime">{timeAgo(lastChattingTime)}</div>
    </div>
  );
}
