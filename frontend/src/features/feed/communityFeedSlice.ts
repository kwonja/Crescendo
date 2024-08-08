import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { FeedInfo, FeedListResponse, getFeedListParams } from '../../interface/feed';
import { getCommunityFeedListAPI } from '../../apis/feed';
import { PromiseStatus } from './feedSlice';
import { RootState } from '../../store/store';

interface CommunityFeedState {
  feedList: FeedInfo[];
  status: PromiseStatus;
  error: string | undefined;
  page: number;
  filterCondition: string;
  sortCondition: string;
  searchCondition: string;
  keyword: string;
}
const initialState: CommunityFeedState = {
  feedList: [],
  status: '',
  error: '',
  page: 0,
  filterCondition: '필터',
  sortCondition: '정렬',
  searchCondition: '검색',
  keyword: ''
};

export const getFeedList = createAsyncThunk<FeedListResponse,number,{state:RootState}>(
    'communityFeedSlice/getFeedList',
    async (idolGroupId, thunkAPI) => {
        const { page, filterCondition, sortCondition, searchCondition, keyword } = thunkAPI.getState().communityFeed;
        const params:getFeedListParams = {
          idolGroupId,
          page,
          size:3,
          nickname: '',
          content: '',
          sortByFollowed: filterCondition ==='팔로우만',
          sortByLiked: sortCondition === '좋아요순',
        }
        searchCondition==='작성자'?params.nickname=keyword:params.content=keyword;
        const response = await getCommunityFeedListAPI(params);
        return response;
});

const communityFeedSlice = createSlice({
  name: 'communityFeed',
  initialState: initialState,
  reducers: {
    resetState: () => {
      return initialState;
    },

    setFilterCondition: (state, action) => {
      state.filterCondition = action.payload;
    },

    setSortCondition: (state, action) => {
      state.sortCondition = action.payload;
    },

    setSearchCondition: (state, action) => {
      state.searchCondition = action.payload;
    },

    setKeyword: (state, action) => {
      state.keyword = action.payload;
    },

    incrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.feedList.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt += 1;
        feed.isLike = true;
      }
    },
    decrementLike: (state, action: PayloadAction<number>) => {
      const feed = state.feedList.find(feed => feed.feedId === action.payload);
      if (feed) {
        feed.likeCnt -= 1;
        feed.isLike = false;
      }
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getFeedList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getFeedList.fulfilled, (state, action) => {
        state.status = 'success';
        state.feedList = [...state.feedList, ...action.payload.content];
        state.page += 1;
      })
      .addCase(getFeedList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { resetState, setFilterCondition, setSearchCondition, 
  setSortCondition,setKeyword, incrementLike, decrementLike } = communityFeedSlice.actions;
export default communityFeedSlice.reducer;
