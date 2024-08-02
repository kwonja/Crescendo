import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import { communityInfo } from '../../interface/communityList';

// 슬라이스의 상태 타입 정의
type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface CommunityListState {
  items: communityInfo[];
  status: PromiseStatus;
  error: string | null;
  page: number;
}

// 초기 상태 정의
const initialState: CommunityListState = {
  items: [],
  status: '',
  error: null,
  page: 0,
};

// 비동기 함수 정의
export const getCommunityList = createAsyncThunk(
  'communityList/getCommunityList',
  async ({ page, size }: { page: number; size: number }) => {
    const response = await axios.get(`/api/community-list?page=${page}&size=${size}`);
    return response.data as communityInfo[];
  },
);

// 슬라이스 정의
const communityListSlice = createSlice({
  name: 'communityList',
  initialState,
  reducers: {
    resetPage(state) {
      state.page = 1;
      state.items = [];
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getCommunityList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getCommunityList.fulfilled, (state, action: PayloadAction<communityInfo[]>) => {
        state.status = 'success';
        state.items = [...state.items, ...action.payload];
        state.page += 1; // page 값 증가
      })
      .addCase(getCommunityList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || 'Failed to fetch community list';
      });
  },
});

export const { resetPage } = communityListSlice.actions;

export default communityListSlice.reducer;
