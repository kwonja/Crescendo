//Ducks 패턴 공부예정

import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { FeedData } from '../../interface/feed';
import { getMyFeedAPI } from '../../apis/user';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface FeedProps {
  myFeedList: FeedData[];
  status: PromiseStatus;
  error: string | undefined;
}
const initialState: FeedProps = {
  myFeedList: [],
  status: '',
  error: '',
};

export const getMyFeedList = createAsyncThunk('feedSlice/getMyFeedList', async (userId: number) => {
  const response = await getMyFeedAPI(0, 10);
  return response;
});

const feedSlice = createSlice({
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

export const { incrementLike, decrementLike } = feedSlice.actions;
export default feedSlice.reducer;
