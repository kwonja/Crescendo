import React, { useCallback, useEffect, useRef, useState } from 'react';
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
import { getMessages, initialMessage, setMessage, setPage } from '../../features/chat/messageSlice';
import { ChatDateTransfer } from '../../utils/ChatDateTransfer';
import { Message } from '../../interface/chat';

export default function Chatroom() {
  const client = useRef<CompatClient | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const [isScroll, setScroll] = useState<boolean>(false);
  const { dmGroupId, opponentNickName } = useAppSelector(state => state.chatroom.selectedGroup);
  const { messageList, currentPage } = useAppSelector(state => state.message);
  const messageListRef = useRef<HTMLDivElement>(null);
  const [prevScrollHeight, setPrevScrollHeight] = useState(0);
  const dispatch = useAppDispatch();

  const connect = useCallback(() => {
    client.current = Stomp.over(() => new SockJS(`${BASE_URL}/ws`));

    client.current.connect(
      {},
      (frame: string) => {
        client.current?.subscribe(`/topic/messages/${dmGroupId}`, content => {
          const newMessage: Message = JSON.parse(content.body);
          setScroll(true);
          dispatch(setMessage(newMessage));
        });
      },
      (error: any) => {},
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
    setScroll(false);
    dispatch(getMessages({ userId: getUserId(), dmGroupId, page: currentPage, size: 10 }));
  }, [dmGroupId, currentPage, dispatch]);

  useEffect(() => {
    connect();

    return () => {
      if (client.current) {
        client.current.disconnect(() => {
          dispatch(initialMessage());
        });
      }
    };
  }, [connect, dispatch]);
  const handleObserver = useCallback(
    (entries: IntersectionObserverEntry[]) => {
      const target = entries[0];
      if (target.isIntersecting) {
        const currentScrollHeight = messageListRef.current?.scrollHeight || 0;
        setPrevScrollHeight(currentScrollHeight);
        dispatch(setPage());
      }
    },
    [dispatch],
  );

  useEffect(() => {
    //스크롤이 업데이트 된다면
    if (prevScrollHeight > 0) {
      const newScrollHeight = messageListRef.current?.scrollHeight || 0;
      if (messageListRef.current) {
        messageListRef.current.scrollTop = newScrollHeight - prevScrollHeight;
      }
    }

    if (isScroll && messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messageList, prevScrollHeight, isScroll]);

  useEffect(() => {
    const option = {
      root: messageListRef.current,
      threshold: 0.1,
    };
    const observer = new IntersectionObserver(handleObserver, option);
    if (messageListRef.current && messageListRef.current.firstElementChild) {
      observer.observe(messageListRef.current.firstElementChild);
    }
    return () => observer.disconnect();
  }, [handleObserver]);

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

      <div className="messagelist" ref={messageListRef}>
        <div></div>
        {messageList.map((message, index) => {
          const messageDate = new Date(message.createdAt).toLocaleDateString();
          const isNewDate =
            index === 0 ||
            messageDate !== new Date(messageList[index - 1].createdAt).toLocaleDateString();
          return (
            <div key={index}>
              {isNewDate && (
                <div className="date">
                  <Line style={{ width: 20 }} />
                  <div className="text-xs">{ChatDateTransfer(messageDate)}</div>
                  <Line />
                </div>
              )}
              {message.writerId === getUserId() ? (
                <MyMessage message={message} />
              ) : (
                <OtherMessage message={message} />
              )}
            </div>
          );
        })}
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
            <Clip className="w-5 h-5" />
            <Submit className="w-5 h-5" onClick={HandleMessageSend} />
          </div>
        </span>
      </div>
    </div>
  );
}
