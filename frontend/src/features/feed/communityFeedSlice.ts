import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { FeedInfo, FeedListResponse, getFeedListParams } from '../../interface/feed';
import { getCommunityFeedListAPI, toggleFeedLikeAPI } from '../../apis/feed';
import { PromiseStatus } from './feedSlice';
import { RootState } from '../../store/store';

interface CommunityFeedState {
  feedList: FeedInfo[];
  status: PromiseStatus;
  error: string | undefined;
  page: number;
  hasMore: boolean;
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
  hasMore: true,
  filterCondition: '',
  sortCondition: '',
  searchCondition: '',
  keyword: '',
};

// 피드 가져오기
export const getFeedList = createAsyncThunk<FeedListResponse, number, { state: RootState }>(
  'communityFeedSlice/getFeedList',
  async (idolGroupId, thunkAPI) => {
    const { page, filterCondition, sortCondition, searchCondition, keyword } =
      thunkAPI.getState().communityFeed;
    const params: getFeedListParams = {
      'idol-group-id': idolGroupId,
      page,
      size: 3,
      nickname: '',
      content: '',
      sortByFollowed: filterCondition === '팔로우만',
      sortByLiked: sortCondition === '좋아요순',
    };
    searchCondition === '작성자' ? (params.nickname = keyword) : (params.content = keyword);
    const response = await getCommunityFeedListAPI(params);
    return response;
  },
);

// 피드 하트 클릭
export const toggleFeedLike = createAsyncThunk(
  'favoriteRankList/toggleIsLike',
  async (feedId: number, { rejectWithValue }) => {
    try {
      await toggleFeedLikeAPI(feedId);
      return feedId;
    } catch (error) {
      return rejectWithValue('Failed to toggle feed-like');
    }
  },
);

const communityFeedSlice = createSlice({
  name: 'communityFeed',
  initialState: initialState,
  reducers: {
    resetState: () => {
      return initialState;
    },

    setFilterCondition: (state, action: PayloadAction<string>) => {
      state.feedList = [];
      state.status = '';
      state.error = '';
      state.page = 0;
      state.hasMore = true;
      state.filterCondition = action.payload;
    },

    setSortCondition: (state, action: PayloadAction<string>) => {
      state.feedList = [];
      state.status = '';
      state.error = '';
      state.page = 0;
      state.hasMore = true;
      state.sortCondition = action.payload;
    },

    searchFeed: (state, action: PayloadAction<{ searchOption: string; searchKeyword: string }>) => {
      state.feedList = [];
      state.status = '';
      state.error = '';
      state.page = 0;
      state.hasMore = true;
      state.searchCondition = action.payload.searchOption;
      state.keyword = action.payload.searchKeyword;
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
        state.hasMore = !action.payload.last;
      })
      .addCase(getFeedList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(toggleFeedLike.fulfilled, (state, action: PayloadAction<number>) => {
        const feedId = action.payload;
        const feed = state.feedList.find(feed => feed.feedId === feedId);
        if (feed) {
          feed.isLike = !feed.isLike;
          feed.isLike ? feed.likeCnt++ : feed.likeCnt--;
        }
      })
      .addCase(toggleFeedLike.rejected, (state, action) => {
        state.error = action.payload as string;
      });
  },
});

export const { resetState, setFilterCondition, setSortCondition, searchFeed } =
  communityFeedSlice.actions;
export default communityFeedSlice.reducer;
