import React, { useCallback, useEffect, useRef } from 'react';
import { ReactComponent as Back } from '../../assets/images/Chat/back.svg';
import { ReactComponent as Hamburger } from '../../assets/images/Chat/hamburger.svg';
import { ReactComponent as Line } from '../../assets/images/Chat/line.svg';
import { ReactComponent as Clip } from '../../assets/images/Chat/clip.svg';
import { ReactComponent as Submit } from '../../assets/images/Chat/submit.svg';
import MyMessage from './MyMessage';
import OtherMessage from './OtherMessage';
import { BASE_URL, getUserId } from '../../apis/core';
import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { setIsSelected, setSelectedGroup } from '../../features/chat/chatroomSlice';
import { getMessages, setMessage } from '../../features/chat/messageSlice';
import { ChatDateTransfer } from '../../utils/ChatDateTransfer';

export default function Chatroom() {
  const client = useRef<CompatClient | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const { dmGroupId, opponentNickName, lastChattingTime } = useAppSelector(
    state => state.chatroom.selectedGroup,
  );
  const { messageList } = useAppSelector(state => state.message);
  const messageListRef = useRef<HTMLDivElement>(null);
  const dispatch = useAppDispatch();

  const connect = useCallback(() => {
    client.current = Stomp.over(() => new SockJS(`${BASE_URL}/ws`));

    client.current.connect(
      {},
      (frame: string) => {
        // console.log('Connected: ' + frame);
        client.current?.subscribe(`/topic/messages/${dmGroupId}`, content => {
          const newMessage = JSON.parse(content.body);
          dispatch(setMessage(newMessage));
        });
        dispatch(getMessages({ userId: getUserId(), dmGroupId }));
      },
      (error: any) => {
        console.error('Connection error: ', error);
      },
    );
  }, [dmGroupId, dispatch]);

  const HandleMessageSend = () => {
    const message = inputRef.current!.value;
    client.current!.send(
      '/app/message',
      {},
      JSON.stringify({
        dmGroupId: dmGroupId,
        message: message,
        writerId: getUserId(),
      }),
    );
    inputRef.current!.value = '';
  };
  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      HandleMessageSend();
    }
  };

  useEffect(() => {
    connect();

    return () => {
      if (client.current) {
        client.current.disconnect(() => {
          console.log('Disconnected');
        });
      }
    };
  }, [connect]);

  useEffect(() => {
    if (messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messageList]);
  return (
    <div className="chatroom">
      <div className="upper">
        <div className="back">
          <Back
            onClick={() => {
              dispatch(setIsSelected(false));
              dispatch(
                setSelectedGroup({
                  dmGroupId: 0,
                  opponentId: 0,
                  opponentProfilePath: '',
                  opponentNickName: '',
                  lastChatting: '',
                  lastChattingTime: '',
                }),
              );
            }}
          />
        </div>
        <div>{opponentNickName}</div>
        <Hamburger />
      </div>
      <div className="date">
        <Line style={{ width: 20 }} />
        <div>{ChatDateTransfer(lastChattingTime)}</div>
        <Line />
      </div>
      <div className="messagelist" ref={messageListRef}>
        {messageList.map(message => (
          <div key={message.dmMessageId}>
            {message.writerId === getUserId() ? (
              <MyMessage message={message} />
            ) : (
              <OtherMessage message={message} />
            )}
          </div>
        ))}
      </div>
      <div className="send-container">
        <span>
          <input
            type="text"
            className="search-input"
            placeholder="여기에 입력하세요"
            ref={inputRef}
            onKeyDown={handleKeyDown}
          />
          <div className="send-icon">
            <Clip style={{ width: '24px', height: '24px' }} />
            <Submit style={{ width: '24px', height: '24px' }} onClick={HandleMessageSend} />
          </div>
        </span>
      </div>
    </div>
  );
}
