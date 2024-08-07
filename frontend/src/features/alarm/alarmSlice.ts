import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { getAlarmAPI } from '../../apis/alarm';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';
interface Alarm {
  alarmId: number;
  alarmChannelId: number;
  relatedId: number;
  content: string;
  isRead: boolean;
  createdAt: string;
}
interface FeedProps {
  alramLsit: Alarm[];
  status: PromiseStatus;
  error: string | undefined;
}
const inistalState: FeedProps = {
  alramLsit: [],
  status: '',
  error: '',
};
export const getAlarm = createAsyncThunk('feedSlice/getMyFeedList', async (userId: number) => {
  const response = await getAlarmAPI(0, 10);
  return response;
});

const alarmSlice = createSlice({
  name: 'feed',
  initialState: inistalState,
  reducers: {
    // incrementLike: (state, action: PayloadAction<number>) => {
    //   const feed = state.myFeedList.find(feed => feed.feedId === action.payload);
    //   if (feed) {
    //     feed.likeCnt += 1;
    //     feed.isLike = true;
    //   }
    // },
    // decrementLike: (state, action: PayloadAction<number>) => {
    //   const feed = state.myFeedList.find(feed => feed.feedId === action.payload);
    //   if (feed) {
    //     feed.likeCnt -= 1;
    //     feed.isLike = false;
    //   }
    // },
  },
  extraReducers(builder) {
    // builder
    //   .addCase(getMyFeedList.pending, state => {
    //     state.status = 'loading';
    //   })
    //   .addCase(getMyFeedList.fulfilled, (state, action) => {
    //     state.status = 'success';
    //     state.myFeedList = action.payload.content;
    //   })
    //   .addCase(getMyFeedList.rejected, (state, action) => {
    //     state.status = 'failed';
    //     state.error = action.error.message;
    //   });
  },
});

// export const { incrementLike, decrementLike } = feedSlice.actions;
export default alarmSlice.reducer;
