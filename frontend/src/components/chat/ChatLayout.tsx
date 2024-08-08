import React, { useEffect, useRef } from 'react';
import ChatRoomListItem from './ChatRoomListItem';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import {
  getUserChatRoomList,
  setIsSelected,
  setLastChatting,
  setSelectedGroup,
} from '../../features/chat/chatroomSlice';
import { ChatRoom, Message } from '../../interface/chat';
import { CompatClient, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BASE_URL, getUserId } from '../../apis/core';

export default function ChatLayout() {
  const { chatRoomList } = useAppSelector(state => state.chatroom);
  const client = useRef<CompatClient | null>(null);
  const dispatch = useAppDispatch();

  const HandleClick = (Group: ChatRoom) => {
    dispatch(setIsSelected(true));
    dispatch(setSelectedGroup(Group));
  };
  useEffect(() => {
    const promise = dispatch(getUserChatRoomList());
    return () => promise.abort();
  }, [dispatch]);

  // WebSocket 연결 설정
  useEffect(() => {
    client.current = Stomp.over(() => new SockJS(`${BASE_URL}/ws`));

    client.current.connect(
      {},
      (frame: string) => {
        // 모든 채팅방에 대한 구독 설정
        chatRoomList.forEach(room => {
          client.current?.subscribe(`/topic/messages/${room.dmGroupId}`, content => {
            const newMessage: Message = JSON.parse(content.body);
            if (newMessage.writerId !== getUserId()) {
              dispatch(
                setLastChatting({
                  dmGroupId: room.dmGroupId,
                  opponentId: newMessage.writerId,
                  opponentProfilePath: newMessage.writerProfilePath,
                  opponentNickName: newMessage.writerNickName,
                  lastChatting: newMessage.message,
                  lastChattingTime: newMessage.createdAt,
                }),
              );
            }
          });
        });
      },
      (error: any) => {},
    );

    return () => {
      if (client.current) {
        client.current.disconnect();
      }
    };
  }, [chatRoomList, dispatch]);

  return (
    <div className="chatlayout">
      <div className="title">채팅목록</div>

      <div className="chatroomlist">
        {chatRoomList.map((room, index) => (
          <ChatRoomListItem key={index} room={room} HandleClick={HandleClick} />
        ))}
      </div>
    </div>
  );
}
