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
  const { selectedGroup } = useAppSelector(state => state.chatroom);

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
        client.current?.subscribe(`/topic/messages/${getUserId()}`, content => {
          const newMessage: Message = JSON.parse(content.body);
          dispatch(
            setLastChatting({
              dmGroupId: newMessage.dmGroupId,
              opponentId: newMessage.writerId,
              opponentProfilePath: newMessage.writerProfilePath,
              opponentNickName: newMessage.writerNickName,
              lastChatting: newMessage.message,
              lastChattingTime: newMessage.createdAt,
            }),
          );
          if (newMessage.dmGroupId !== selectedGroup.dmGroupId) {
            dispatch(incrementUnReadChat(newMessage.dmGroupId));
          }
        });
      },
      (error: any) => {},
    );

    return () => {
      if (client.current) {
        client.current.disconnect();
      }
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, selectedGroup]);

  return <></>;
}
