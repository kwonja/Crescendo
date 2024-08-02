import React, { useEffect } from 'react';
import ChatRoomListItem from './ChatRoomListItem';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getUserChatRoomList, setIsSelected, setSelectedGroupId } from '../../features/chat/chatroomSlice';

export default function ChatLayout() {

  const { chatRoomList }= useAppSelector( (state)=>state.chatroom);
  const dispatch = useAppDispatch();

  const HandleClick= (GroudId : number)=>{
    dispatch(setIsSelected(true));
    dispatch(setSelectedGroupId(GroudId));
  }
  useEffect( ()=>{
    const promise = dispatch(getUserChatRoomList());
    return () => promise.abort();
  },[dispatch])
  return (
    <div className="chatlayout">
      <div className="title">채팅목록</div>

      <div className="chatroomlist">
        {
          chatRoomList.map((room, index) => (
            <ChatRoomListItem key={index} room={room} HandleClick={HandleClick}/>
          ))
        }
      </div>
    </div>
  );
}
