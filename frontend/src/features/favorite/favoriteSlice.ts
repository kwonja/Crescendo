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
  favoriteRankList: [{// 임시 데이터
    writerId: 1,
    writerNickname: '카페모카',
    writerProfilePath: 'https://i.ibb.co/qnNRwbY/haerin.jpg',
    favoriteRankId: 1,
    favoriteIdolImagePath: 'https://i.ibb.co/JFqgZck/minji.jpg',
    likeCnt: 0,
    isLike: false,
    createdAt: "2024-08-07T02:57:20.882Z"
  }],
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
    const page = state.page;
    const response = await getFavoriteRankListAPI(page, 4);
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
            state.sortByVotes = true;    
        }
        else if (action.payload === '좋아요순') {
            state.sortByVotes = false;    
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
