//Ducks 패턴 공부예정

import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { MyFeedInfo } from '../../interface/feed';
import { getMyFanArtAPI, getMyFeedAPI } from '../../apis/user';
import { MyFanArtInfo, MyGoodsInfo } from '../../interface/gallery';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface MyFeedState {
  myFeedList: MyFeedInfo[];
  myFanArtList: MyFanArtInfo[];
  myGoodsList: MyGoodsInfo[];
  status: PromiseStatus;
  error: string | undefined;
  hasMore: boolean;
  page: number;
}
const initialState: MyFeedState = {
  myFeedList: [],
  myFanArtList: [],
  myGoodsList: [],
  status: '',
  error: '',
  hasMore: true,
  page: 0,
};

export const getMyFeedList = createAsyncThunk('myFeedSlice/getMyFeedList', async (userId: number) => {
  const response = await getMyFeedAPI(userId, 0, 3);
  return response;
});

export const getMyFanArtList = createAsyncThunk('myFeedSlice/getMyFanArtList', async (userId: number) => {
  const response = await getMyFanArtAPI(userId, 0, 3);
  return response;
});

export const getMyGoodsList = createAsyncThunk('myFeedSlice/getMyGoodsList', async (userId: number) => {
  const response = await getMyFanArtAPI(userId, 0, 3);
  return response;
});

const myFeedSlice = createSlice({
  name: 'feed',
  initialState: initialState,
  reducers: {
    resetState: () => {
      return initialState;
    },

    incrementLike: (state, action: PayloadAction<{type:'feed'|'fanArt'|'goods', id:number}>) => {
      switch(action.payload.type) {
        case 'feed':
          const feed = state.myFeedList.find(feed => feed.feedId === action.payload.id);
          if (feed) {
            feed.likeCnt += 1;
            feed.isLike = true;
          }
          break;
        case 'fanArt':
          const fanArt = state.myFanArtList.find(fanArt => fanArt.fanArtId === action.payload.id);
          if (fanArt) {
            fanArt.likeCnt += 1;
            fanArt.isLike = true;
          }
          break;
        case 'goods':
          const goods = state.myGoodsList.find(goods => goods.goodsId === action.payload.id);
          if (goods) {
            goods.likeCnt += 1;
            goods.isLike = true;
          }
          break;
      }
    },
    decrementLike: (state, action: PayloadAction<{type:'feed'|'fanArt'|'goods', id:number}>) => {
      switch(action.payload.type) {
        case 'feed':
          const feed = state.myFeedList.find(feed => feed.feedId === action.payload.id);
          if (feed) {
            feed.likeCnt -= 1;
            feed.isLike = false;
          }
          break;
        case 'fanArt':
          const fanArt = state.myFanArtList.find(fanArt => fanArt.fanArtId === action.payload.id);
          if (fanArt) {
            fanArt.likeCnt -= 1;
            fanArt.isLike = false;
          }
          break;
        case 'goods':
          const goods = state.myGoodsList.find(goods => goods.goodsId === action.payload.id);
          if (goods) {
            goods.likeCnt -= 1;
            goods.isLike = false;
          }
          break;
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
        state.myFeedList = [...state.myFeedList, ...action.payload.content];
        state.page += 1;
        state.hasMore = !action.payload.last;
      })
      .addCase(getMyFeedList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(getMyFanArtList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getMyFanArtList.fulfilled, (state, action) => {
        state.status = 'success';
        state.myFanArtList = [...state.myFanArtList, ...action.payload.content];
        state.page += 1;
        state.hasMore = !action.payload.last;
      })
      .addCase(getMyFanArtList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(getMyGoodsList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getMyGoodsList.fulfilled, (state, action) => {
        state.status = 'success';
        state.myGoodsList = [...state.myGoodsList, ...action.payload.content];
        state.page += 1;
        state.hasMore = !action.payload.last;
      })
      .addCase(getMyGoodsList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { resetState, incrementLike, decrementLike } = myFeedSlice.actions;
export default myFeedSlice.reducer;
