//Ducks 패턴 공부예정

import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { ChatRoom } from '../../interface/chat';
import { chatroomlistAPI } from '../../apis/chat';
import { PromiseStatus } from '../follow/followerSlice';

interface chatProps {
  chatRoomList: ChatRoom[];
  status: PromiseStatus;
  error: string | undefined;
  isSelected: boolean;
  selectedGroupId: number;
  writerId: number;
}
const inistalState: chatProps = {
  chatRoomList: [],
  status: '',
  error: '',
  isSelected: false,
  selectedGroupId: 0,
  writerId: 0,
};

export const getUserChatRoomList = createAsyncThunk(
  'chatroomSlice/getUserChatRoomList',
  async () => {
    const response = await chatroomlistAPI(1, 1);
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
    setSelectedGroupId: (state, action: PayloadAction<number>) => {
      state.selectedGroupId = action.payload;
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getUserChatRoomList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getUserChatRoomList.fulfilled, (state, action) => {
        state.status = 'success';
        console.log(action.payload);
        state.chatRoomList = action.payload;
      })
      .addCase(getUserChatRoomList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setIsSelected, setSelectedGroupId } = chatroomSlice.actions;
export default chatroomSlice.reducer;
