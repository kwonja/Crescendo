import React, { useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { Message } from '../../interface/chat';
import SockJS from 'sockjs-client';
import { BASE_URL, getUserId } from '../../apis/core';
import {
  getUserChatRoomList,
  incrementUnReadChat,
  setLastChatting,
} from '../../features/chat/chatroomSlice';

export default function ChatConnect() {
  const { chatRoomList } = useAppSelector(state => state.chatroom);
  const dispatch = useAppDispatch();
  const client = useRef<CompatClient | null>(null);

  useEffect(() => {
    const promise = dispatch(getUserChatRoomList());
    return () => promise.abort();
  }, [dispatch]);

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
              dispatch(incrementUnReadChat());
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

  return <></>;
}
