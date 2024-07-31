//Ducks 패턴 공부예정

import { createSlice } from '@reduxjs/toolkit';
import { FeedData } from '../../interface/feed';


const inistalState: FeedData[] = [{
    userId: 0,
    profileImagePath: "", 
    nickname: "Nickname", 
    createdAt: "2024.01.01", 
    updatedAt: "", 
    likeCnt: 0, 
    isLike: false, 
    feedImagePathList: [], 
    content: "", 
    commentCnt: 0,
    tagList: ['뉴진스','2주년'],
}];

const mypageSlice = createSlice({
  name: 'feed',
  initialState: inistalState,
  reducers: {

  },
});

// export const { updateFeed } = mypageSlice.actions;
export default mypageSlice.reducer;
