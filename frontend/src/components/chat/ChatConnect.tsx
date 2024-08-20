import React, { useEffect  } from 'react';
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
import { setClient, setConnected } from '../../features/chat/webSocketSlice';

export default function ChatConnect() {
  const { selectedGroup } = useAppSelector(state => state.chatroom);

  const dispatch = useAppDispatch();

  useEffect(() => {
    const promise = dispatch(getUserChatRoomList());
    return () => promise.abort();
  }, [dispatch]);

  useEffect(() => {
    const client: CompatClient = Stomp.over(() => new SockJS(`${BASE_URL}/ws`));

    client.connect(
      {},
      (frame: string) => {
        dispatch(setClient(client));
        dispatch(setConnected(true));
        dispatch(getUserChatRoomList());
        client.subscribe(`/topic/messages/${getUserId()}`, content => {
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
            dispatch(getUserChatRoomList());
          }
        });
      },
      (error: any) => {},
    );

    return () => {
      client.disconnect(() => {
        dispatch(setConnected(false));
      });
    };
  }, [dispatch, selectedGroup]);

  return <></>;
}
