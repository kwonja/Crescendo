import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { communityInfo, communityListResponse } from '../../interface/communityList';
import { getCommunityListAPI } from '../../apis/communityList';

// 슬라이스의 상태 타입 정의
type PromiseStatus = 'loading' | 'success' | 'failed' | '';

interface CommunityListState {
  communityList: communityInfo[];
  status: PromiseStatus;
  error: string | null;
  page: number;
  hasMore: boolean;
}

// 초기 상태 정의
const initialState: CommunityListState = {
  communityList: [],
  status: '',
  error: null,
  page: 0,
  hasMore: true
};

// 비동기 함수 정의
export const getCommunityList = createAsyncThunk(
  'communityList/getCommunityList',
  async ({ page, size }: { page: number; size: number }) => {
    const response = await getCommunityListAPI(page,size);
    return response;
  },
);

// 슬라이스 정의
const communityListSlice = createSlice({
  name: 'communityList',
  initialState,
  reducers: {
    resetPage(state) {
      state.page = 0;
      state.communityList = [];
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getCommunityList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getCommunityList.fulfilled, (state, action: PayloadAction<communityListResponse>) => {
        state.status = 'success';
        state.hasMore = !action.payload.last;
        state.communityList = [...state.communityList, ...action.payload.content];
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
