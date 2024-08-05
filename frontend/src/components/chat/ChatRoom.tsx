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

export default function Chatroom() {
  const client = useRef<CompatClient | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const [isScroll,setScroll] = useState<boolean>(true);
  const { dmGroupId, opponentNickName, lastChattingTime } = useAppSelector(
    state => state.chatroom.selectedGroup,
  );
  const { messageList,currentPage } = useAppSelector(state => state.message);
  const messageListRef = useRef<HTMLDivElement>(null);
  const dispatch = useAppDispatch();

  const connect = useCallback(() => {
    client.current = Stomp.over(() => new SockJS(`${BASE_URL}/ws`));

    client.current.connect(
      {},
      (frame: string) => {
        client.current?.subscribe(`/topic/messages/${dmGroupId}`, content => {
          const newMessage = JSON.parse(content.body);
          setScroll(true);
          dispatch(setMessage(newMessage));
        });
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


  useEffect( ()=>{
    dispatch(getMessages({ userId: getUserId(), dmGroupId, page : currentPage, size : 10}));
  },[dmGroupId,currentPage,dispatch])
  
  useEffect(() => {
    connect();

    return () => {
      if (client.current) {
        client.current.disconnect(() => {
          dispatch(initialMessage());
          console.log('Disconnected');
        });
      }
    };
  }, [connect,dispatch]);

  useEffect(() => {
    if (isScroll && messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messageList,isScroll]);



  const handleObserver = useCallback((entries : IntersectionObserverEntry[]) => {
    const target = entries[0];
    console.log(target)
    if (target.isIntersecting) {
      dispatch(setPage());
      setScroll(false);
    }
  }, [dispatch]);


  useEffect(() => {
    const option = {
      root: messageListRef.current,
      threshold: 0.1,
    };
    const observer = new IntersectionObserver(handleObserver, option);
    console.log(messageListRef?.current?.firstElementChild)
    if (messageListRef.current && messageListRef.current.firstElementChild) {
      observer.observe(messageListRef.current.firstElementChild);
    }
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
      <div className="date">
        <Line style={{ width: 20 }} />
        <div>{ChatDateTransfer(lastChattingTime)}</div>
        <Line />
      </div>
      <div className="messagelist" ref={messageListRef}>
        <div></div>
        {messageList.map( (message,index) => (
          <div key={index}>
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
