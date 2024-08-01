import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

// communityList 타입 정의
interface CommunityItem {
  idolGroupId: number;
  name: string;
  profile: string;
}

// 슬라이스의 상태 타입 정의
interface CommunityListState {
  items: CommunityItem[];
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
  page: number;
}

// 초기 상태 정의
const initialState: CommunityListState = {
  items: [],
  status: 'idle',
  error: null,
  page: 0,
};

// 비동기 함수 정의
export const fetchCommunityList = createAsyncThunk(
  'communityList/fetchCommunityList',
  async ({ page, size }: { page: number; size: number }) => {
    const response = await axios.get(`/api/community-list?page=${page}&size=${size}`); // 이부분은 수정하겟지..
    return response.data as CommunityItem[];
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
      .addCase(fetchCommunityList.pending, state => {
        state.status = 'loading';
      })
      .addCase(fetchCommunityList.fulfilled, (state, action: PayloadAction<CommunityItem[]>) => {
        state.status = 'succeeded';
        state.items = [...state.items, ...action.payload];
        state.page += 1; // page 값 증가
      })
      .addCase(fetchCommunityList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || 'Failed to fetch community list';
      });
  },
});

export const { resetPage } = communityListSlice.actions;

export default communityListSlice.reducer;
