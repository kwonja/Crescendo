//Ducks 패턴 공부예정

import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { follow } from '../../interface/follow';
import { followingAPI } from '../../apis/follow';

type PromiseStatus = 'loading' | 'sucess' | 'failed' | '';
interface followProps {
  followingList: follow[];
  status: PromiseStatus;
  error: string | undefined;
}

//createAsyncThunk는 비동기 작업을 도와주는 액션함수이기때문에
//타입이 있어야한다.
export const getUserFollowing = createAsyncThunk(
  'followSlice/getUserFollowing',
  async (userId: number) => {
    const response = await followingAPI(userId);
    return response;
  },
);

const inistalState: followProps = {
  followingList: [
    {
      userId: 1,
      nickname: 'Nickname1',
      profilePath: 'https://cdn.topstarnews.net/news/photo/202301/15040596_1067813_363.jpg',
    },
    {
      userId: 1,
      nickname: 'Nickname1',
      profilePath: 'https://cdn.topstarnews.net/news/photo/202301/15040596_1067813_363.jpg',
    },
    {
      userId: 1,
      nickname: 'Nickname1',
      profilePath: 'https://cdn.topstarnews.net/news/photo/202301/15040596_1067813_363.jpg',
    },
  ],
  status: '',
  error: '',
};

const followerSlice = createSlice({
  name: 'follwer',
  initialState: inistalState,
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(getUserFollowing.pending, state => {
        state.status = 'loading';
      })
      .addCase(getUserFollowing.fulfilled, (state, action) => {
        state.status = 'sucess';
        state.followingList = action.payload.followingList;
      })
      .addCase(getUserFollowing.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export default followerSlice.reducer;
