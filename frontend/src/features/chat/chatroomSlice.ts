//Ducks 패턴 공부예정

import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { ChatRoom } from '../../interface/chat';
import { chatroomlistAPI } from '../../apis/chat';
import { PromiseStatus } from '../mypage/followerSlice';

interface chatProps {
  chatRoomList: ChatRoom[];
  status: PromiseStatus;
  error: string | undefined;
  isSelected: boolean;
  selectedGroup: ChatRoom;
  writerId: number;
  unReadChat : number;
}
const inistalState: chatProps = {
  chatRoomList: [],
  status: '',
  error: '',
  isSelected: false,
  selectedGroup: {
    dmGroupId: 0,
    opponentId: 0,
    opponentProfilePath: '',
    opponentNickName: '',
    lastChatting: '',
    lastChattingTime: '',
  },
  writerId: 0,
  unReadChat : 0,
};

export const getUserChatRoomList = createAsyncThunk(
  'chatroomSlice/getUserChatRoomList',
  async () => {
    const response = await chatroomlistAPI();
    return response;
  },
);

const chatroomSlice = createSlice({
  name: 'chat',
  initialState: inistalState,
  reducers: {
    setIsSelected: (state, action: PayloadAction<boolean>) => {
      state.isSelected = action.payload;
    },
    setSelectedGroup: (state, action: PayloadAction<ChatRoom>) => {
      state.selectedGroup = action.payload;
    },
    setLastChatting: (state, action: PayloadAction<ChatRoom>) => {
      const index = state.chatRoomList.findIndex(
        chatRoom => chatRoom.dmGroupId === action.payload.dmGroupId,
      );

      if (index !== null) {
        state.chatRoomList[index] = action.payload;
        state.chatRoomList = [...state.chatRoomList];
      }
    },
    incrementUnReadChat: (state) =>{
      state.unReadChat += 1;
    },

    decrementUnReadChat : (state) =>{
      state.unReadChat -= 1;
    }

    
  },
  extraReducers: builder => {
    builder
      .addCase(getUserChatRoomList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getUserChatRoomList.fulfilled, (state, action) => {
        state.status = 'success';
        state.chatRoomList = action.payload;
      })
      .addCase(getUserChatRoomList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setIsSelected, setSelectedGroup, setLastChatting,incrementUnReadChat,decrementUnReadChat } = chatroomSlice.actions;
export default chatroomSlice.reducer;
