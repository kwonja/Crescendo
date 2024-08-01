//Ducks 패턴 공부예정

import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { follow } from '../../interface/follow';
import { followerAPI } from '../../apis/follow';

type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface followProps {
  followerList: follow[];
  status: PromiseStatus;
  error: string | undefined;
}

//createAsyncThunk는 비동기 작업을 도와주는 액션함수이기때문에
//타입이 있어야한다.
export const getUserFollower = createAsyncThunk(
  'followerSlice/getUserFollower',
  async (userId: number) => {
    const response = await followerAPI(userId);
    console.log(response);
    return response;
  },
);

const inistalState: followProps = {
  followerList: [
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
  name: 'follower',
  initialState: inistalState,
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(getUserFollower.pending, state => {
        state.status = 'loading';
      })
      .addCase(getUserFollower.fulfilled, (state, action) => {
        state.status = 'success';
        console.log(action.payload.followingList);
        state.followerList = action.payload.followerList;
        console.log(state.followerList);
      })
      .addCase(getUserFollower.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export default followerSlice.reducer;
