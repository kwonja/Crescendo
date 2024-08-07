import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { PromiseStatus } from '../follow/followerSlice';
import { favoriteRankInfo, favoriteRankListResponse } from '../../interface/favorite';
import { getFavoriteRankListAPI } from '../../apis/favorite';
import { RootState } from '../../store/store';

interface favoriteState {
  favoriteRankList: favoriteRankInfo[];
  status: PromiseStatus;
  error: string | null;
  page: number;
  hasMore: boolean;
  idolId: number;
  sortByVotes: boolean;
}

const initialState: favoriteState = {
  favoriteRankList: [],
  status: '',
  error: null,
  page: 0,
  hasMore: true,
  idolId: 0,
  sortByVotes: false
};

// 전체 커뮤니티 리스트 가져오는 함수
export const getFavoriteRankList = createAsyncThunk<favoriteRankListResponse,void,{state:RootState}>(
  'communityList/getCommunityList',
  async (_, thunkAPI) => {
    const state = thunkAPI.getState().favorite;
    const response = await getFavoriteRankListAPI(state.page, 3, state.idolId, state.sortByVotes);
    return response;
  },
);

const favoriteSlice = createSlice({
  name: 'favoriteRankList',
  initialState,
  reducers: {
    resetPage(state) {
      state.page = 0;
      state.favoriteRankList = [];
      state.hasMore = true;
      state.status = '';
      state.error = null;
    },

    setIdolId(state, action) {
      state.idolId = action.payload;
    },

    setsortOption(state, action) {
        if (action.payload === '최신순' || action.payload === '정렬') {
            state.sortByVotes = false;    
        }
        else if (action.payload === '좋아요순') {
            state.sortByVotes = true;    
        }
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getFavoriteRankList.pending, state => {
        state.status = 'loading';
      })
      .addCase(
        getFavoriteRankList.fulfilled,
        (state, action: PayloadAction<favoriteRankListResponse>) => {
          state.status = 'success';
          state.hasMore = !action.payload.last;
          state.favoriteRankList = [...state.favoriteRankList, ...action.payload.content];
          state.page += 1;
        },
      )
      .addCase(getFavoriteRankList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || 'Failed to fetch favorite idol photo list';
      });
  },
});

export const { resetPage, setIdolId, setsortOption} = favoriteSlice.actions;

export default favoriteSlice.reducer;
