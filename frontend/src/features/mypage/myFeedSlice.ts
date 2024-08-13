//Ducks 패턴 공부예정

import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { FeedInfo } from '../../interface/feed';
import { getMyFeedAPI } from '../../apis/user';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface MyFeedState {
  myFeedList: FeedInfo[];
  status: PromiseStatus;
  error: string | undefined;
}
const initialState: MyFeedState = {
  myFeedList: [],
  status: '',
  error: '',
};

export const getMyFeedList = createAsyncThunk('myFeedSlice/getMyFeedList', async (userId: number) => {
  const response = await getMyFeedAPI(userId, 0, 10);
  return response;
});

const myFeedSlice = createSlice({
  name: 'feed',
  initialState: initialState,
  reducers: {
    incrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.myFeedList.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt += 1;
        feed.isLike = true;
      }
    },
    decrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.myFeedList.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt -= 1;
        feed.isLike = false;
      }
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getMyFeedList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getMyFeedList.fulfilled, (state, action) => {
        state.status = 'success';
        state.myFeedList = [...action.payload.content];
      })
      .addCase(getMyFeedList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { incrementLike, decrementLike } = myFeedSlice.actions;
export default myFeedSlice.reducer;
