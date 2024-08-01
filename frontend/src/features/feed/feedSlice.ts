//Ducks 패턴 공부예정

import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { FeedData } from '../../interface/feed';

const inistalState: FeedData[] = [
  {
    feedId: 0,
    userId: 0,
    profileImagePath: '',
    nickname: 'Nickname',
    createdAt: '2024.01.01',
    updatedAt: '',
    likeCnt: 0,
    isLike: false,
    feedImagePathList: [],
    content: '뉴진스와 버니즈의 2주년!!',
    commentCnt: 0,
    tagList: ['뉴진스', '2주년'],
  },
  {
    feedId: 1,
    userId: 1,
    profileImagePath: '',
    nickname: 'Nickname',
    createdAt: '2024.01.01',
    updatedAt: '',
    likeCnt: 0,
    isLike: false,
    feedImagePathList: [],
    content: '뉴진스와 버니즈의 2주년!!',
    commentCnt: 0,
    tagList: ['뉴진스', '2주년'],
  },
  {
    feedId: 2,
    userId: 2,
    profileImagePath: '',
    nickname: 'Nickname',
    createdAt: '2024.01.01',
    updatedAt: '',
    likeCnt: 0,
    isLike: false,
    feedImagePathList: [],
    content: '뉴진스와 버니즈의 2주년!!',
    commentCnt: 0,
    tagList: ['뉴진스', '2주년'],
  },
];

const feedSlice = createSlice({
  name: 'feed',
  initialState: inistalState,
  reducers: {
    incrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt += 1;
        feed.isLike = true;
      }
    },
    decrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt -= 1;
        feed.isLike = false;
      }
    },
  },
});

export const { incrementLike, decrementLike } = feedSlice.actions;
export default feedSlice.reducer;
