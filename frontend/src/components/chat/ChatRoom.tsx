import React, { useEffect, useRef, useState  } from 'react';
import { ReactComponent as Back } from '../../assets/images/Chat/back.svg';
import { ReactComponent as Hamburger } from '../../assets/images/Chat/hamburger.svg';
import { ReactComponent as Line } from '../../assets/images/Chat/line.svg';
import { ReactComponent as Clip } from '../../assets/images/Chat/clip.svg';
import { ReactComponent as Submit } from '../../assets/images/Chat/submit.svg';
import MyMessage from './MyMessage';
import OtherMessage from './OtherMessage';
import { BASE_URL } from '../../apis/core';
import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';

export default function Chatroom() {
  const [messages, setMessages] = useState<string[]>(['메세지입니다']);
  const client = useRef<CompatClient | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const connect = () => {
    client.current = Stomp.over( () => new SockJS(`${BASE_URL}/ws`));

    client.current.connect({}, (frame : string) => {
        console.log('Connected: ' + frame);
        client.current?.subscribe('/topic/messages/1', (content) => {
          console.log('Message received: ', content);
          const newMessage = JSON.parse(content.body).content;
          setMessages((prev) => [...prev, newMessage]);
        });
    }, (error: any) => {
        console.error('Connection error: ', error);
    });
};


const HandleMessageSend = ()=>{
  const message = inputRef.current!.value;
  client.current!.send(
    "/app/message",
    {},
    JSON.stringify({
      dmGroupId: 1,
      content: message,
      senderId: 123,
    })
  );
  inputRef.current!.value = '';
}

useEffect( ()=>{
  connect();

  return () => {
    if (client.current) {
        client.current.disconnect(() => {
            console.log('Disconnected');
        });
    }
};

},[])
  

  return (
    <div className="chatroom">
      <div className="upper">
        <Back />
        <div>권자몬</div>
        <Hamburger />
      </div>
      <div className="date">
        <Line style={{ width: 20 }} />
        <div>2024 년 07 월 24 일</div>
        <Line />
      </div>
      <OtherMessage />
      <MyMessage />
      {messages}
      <div className="send-container">
        <span>
          <input type="text" className="search-input" placeholder="여기에 입력하세요" 
          ref={inputRef}
          
          />
          <div className="send-icon">
            <Clip style={{ width: '24px', height: '24px' }} />
            <Submit style={{ width: '24px', height: '24px' }} onClick={HandleMessageSend}/>
          </div>
        </span>
      </div>
    
      
    </div>
  );
}
