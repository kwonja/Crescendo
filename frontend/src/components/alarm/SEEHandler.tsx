import React, { useEffect, useRef, } from 'react'
import { BASE_URL, getUserId } from '../../apis/core';
import { useAppSelector } from '../../store/hooks/hook';

export default function SEEHandler() {
    
    // const [notifications, setNotifications] = useState<Notification[]>([]);
    // const [unreadCount, setUnreadCount] = useState<number>(0);
    const {isLoggedIn} =  useAppSelector( (state)=> state.auth)
    const sse = useRef<EventSource | null>(null);
    useEffect(() => {

          const connectSSE = () => {

          sse.current = new EventSource(`${BASE_URL}/sse/connect/${getUserId()}`);
    
          sse.current.addEventListener('connect', (e: Event) => {
            const { data } = e as MessageEvent;
            console.log(data);
          });
    
          sse.current.addEventListener('alarm', (e: Event) => {
            const { data } = e as MessageEvent;
            console.log(data);
            // const notification: Notification = JSON.parse(data);
            // setNotifications((prevNotifications) => [...prevNotifications, notification]);
            // setUnreadCount((prevCount) => prevCount + 1);
          });
    
          sse.current.onerror = () => {
            console.log("에러요~")
            sse.current?.close();
            setTimeout(() => {
              if (isLoggedIn) connectSSE();
            }, 3000); // Try to reconnect after 3 seconds
          };
        };
    
        if (isLoggedIn) {
            console.log('실행');
          connectSSE();
        }
    
        return () => {
          sse.current?.close();
        };
      }, [isLoggedIn]);

  
    return (
      <>
      
      </>
    );
  };
