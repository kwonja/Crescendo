//Ducks 패턴 공부예정

import { createSlice } from '@reduxjs/toolkit';

import { Chat } from '../../interface/chat';

interface chatProps {
  messageList: Chat[];
}
const inistalState: chatProps = {
  messageList: [
    {
      dmGroupId: 1,
      content: '안녕하세요',
      writerId: 1,
    },
    {
      dmGroupId: 1,
      content: '안녕하세요',
      writerId: 3,
    },
    {
      dmGroupId: 1,
      content: '안녕하세요',
      writerId: 1,
    },
  ],
};

const chatSlice = createSlice({
  name: 'chat',
  initialState: inistalState,
  reducers: {
    // incrementLike: (state, action: PayloadAction<number>) => {
    //   const feed = state.find(feed => feed.feedId === action.payload);
    //   if (feed) {
    //     feed.likeCnt += 1;
    //     feed.isLike = true;
    //   }
    // },
    // decrementLike: (state, action: PayloadAction<number>) => {
    //   const feed = state.find(feed => feed.feedId === action.payload);
    //   if (feed) {
    //     feed.likeCnt -= 1;
    //     feed.isLike = false;
    //   }
    // },
  },
});

// export const { incrementLike, decrementLike } = feedSlice.actions;
export default chatSlice.reducer;
