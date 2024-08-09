import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { PromiseStatus } from '../follow/followerSlice';
import { FavoriteRankInfo, FavoriteRankListResponse } from '../../interface/favorite';
import { getFavoriteRankListAPI, voteFavoriteRankAPI } from '../../apis/favorite';
import { RootState } from '../../store/store';

interface FavoriteState {
  favoriteRankList: FavoriteRankInfo[];
  status: PromiseStatus;
  error: string | null;
  page: number;
  hasMore: boolean;
  idolId: number;
  sortByVotes: boolean;
}

const initialState: FavoriteState = {
  favoriteRankList: [],
  status: '',
  error: null,
  page: 0,
  hasMore: true,
  idolId: 0,
  sortByVotes: false
};

// 전체 커뮤니티 리스트 가져오는 함수
export const getFavoriteRankList = createAsyncThunk<FavoriteRankListResponse,void,{state:RootState}>(
  'favoriteRankList/getFavoriteRankList',
  async (_, thunkAPI) => {
    const state = thunkAPI.getState().favorite;
    const response = await getFavoriteRankListAPI(state.page, 3, state.idolId, state.sortByVotes);
    return response;
  },
);

// 투표
export const toggleIsLike = createAsyncThunk(
  'favoriteRankList/toggleIsLike',
  async (favoriteRankId:number, { rejectWithValue }) => {
    try {
      await voteFavoriteRankAPI(favoriteRankId);
      return favoriteRankId;
    } catch (error) {
      return rejectWithValue('Failed to vote');
    }

  }
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

    setSortByVotes(state, action) {
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
        (state, action: PayloadAction<FavoriteRankListResponse>) => {
          state.status = 'success';
          state.hasMore = !action.payload.last;
          state.favoriteRankList = [...state.favoriteRankList, ...action.payload.content];
          state.page += 1;
        },
      )
      .addCase(getFavoriteRankList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || 'Failed to fetch favorite idol photo list';
      })
      .addCase(toggleIsLike.fulfilled, (state, action: PayloadAction<number>) => {
        const votedRankId = action.payload;
        const rankPost = state.favoriteRankList.find((rankPost) => rankPost.favoriteRankId === votedRankId);
        if (rankPost) {
          rankPost.isLike = !rankPost.isLike;
          rankPost.isLike?rankPost.likeCnt++:rankPost.likeCnt--;
        }
      })
      .addCase(toggleIsLike.rejected, (state, action) => {
        state.error = action.payload as string;
      });
  },
});

export const { resetPage, setIdolId, setSortByVotes} = favoriteSlice.actions;

export default favoriteSlice.reducer;
