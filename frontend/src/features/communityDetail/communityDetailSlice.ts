import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { FeedInfo, FeedListResponse, getFeedListParams } from '../../interface/feed';
import { FanArtListResponse, GalleryInfo, getGalleryListParams } from '../../interface/gallery';
import { getCommunityFeedListAPI, toggleFeedLikeAPI } from '../../apis/feed';
import { PromiseStatus } from '../feed/feedSlice';
import { RootState } from '../../store/store';
import { getCommunityFanArtListAPI, toggleFanArtLikeAPI } from '../../apis/fanart';

interface CommunityDetailState {
  feedList: FeedInfo[];
  fanArtList: GalleryInfo[],
  goodsList: GalleryInfo[],
  status: PromiseStatus;
  error: string | undefined;
  page: number;
  hasMore: boolean;
  filterCondition: string;
  sortCondition: string;
  searchCondition: string;
  keyword: string;
}
const initialState: CommunityDetailState = {
  feedList: [],
  fanArtList: [],
  goodsList: [],
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
  'communityDetailSlice/getFeedList',
  async (idolGroupId, thunkAPI) => {
    const { page, filterCondition, sortCondition, searchCondition, keyword } =
      thunkAPI.getState().communityDetail;
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

export const getFanArtList = createAsyncThunk<FanArtListResponse, number, { state: RootState }>(
  'communityDetailSlice/getFanArtList',
  async (idolGroupId, thunkAPI) => {
    const { page, filterCondition, sortCondition, searchCondition, keyword } =
      thunkAPI.getState().communityDetail;
    const params: getGalleryListParams = {
      'idol-group-id': idolGroupId,
      page,
      size: 3,
      title: '',
      nickname: '',
      content: '',
      sortByFollowed: filterCondition === '팔로우만',
      sortByLiked: sortCondition === '좋아요순',
    };
    if (searchCondition === '작성자')
      params.nickname = keyword; 
    else if (searchCondition === '내용')
      params.content = keyword;
    else {
      params.title = keyword;
    }
    const response = await getCommunityFanArtListAPI(params);
    return response;
  },
);

// 피드 하트 클릭
export const toggleFeedLike = createAsyncThunk(
  'communityDetailSlice/toggleFeedLike',
  async (feedId: number, { rejectWithValue }) => {
    try {
      await toggleFeedLikeAPI(feedId);
      return feedId;
    } catch (error) {
      return rejectWithValue('Failed to toggle feed-like');
    }
  },
);

// 팬아트 하트 클릭
export const toggleFanArtLike = createAsyncThunk(
  'communityDetailSlice/toggleFanArtLike',
  async (fanArtId: number, { rejectWithValue }) => {
    try {
      await toggleFanArtLikeAPI(fanArtId);
      return fanArtId;
    } catch (error) {
      return rejectWithValue('Failed to toggle fan-art-like');
    }
  },
);

const communityDetailSlice = createSlice({
  name: 'communityDetail',
  initialState: initialState,
  reducers: {
    resetState: () => {
      return initialState;
    },

    setFilterCondition: (state, action: PayloadAction<string>) => {
      if (state.filterCondition !== action.payload) {
        state.feedList = [];
        state.fanArtList = [];
        state.goodsList = [];
        state.status = '';
        state.error = '';
        state.page = 0;
        state.hasMore = true;
        state.filterCondition = action.payload;
      }
    },

    setSortCondition: (state, action: PayloadAction<string>) => {
      if (state.sortCondition !== action.payload) {
        state.feedList = [];
        state.fanArtList = [];
        state.goodsList = [];
        state.status = '';
        state.error = '';
        state.page = 0;
        state.hasMore = true;
        state.sortCondition = action.payload;
      }
    },

    searchFeed: (state, action: PayloadAction<{ searchOption: string; searchKeyword: string }>) => {
      if (state.searchCondition !==action.payload.searchOption || state.keyword !== action.payload.searchKeyword) {
        state.feedList = [];
        state.fanArtList = [];
        state.goodsList = [];
        state.status = '';
        state.error = '';
        state.page = 0;
        state.hasMore = true;
        state.searchCondition = action.payload.searchOption;
        state.keyword = action.payload.searchKeyword;
      }
    },

    updateFeed: (state, action: PayloadAction<{feed:FeedInfo; feedId:number}>) => {
      state.feedList = [...state.feedList.map((feed)=> feed.feedId=== action.payload.feedId?action.payload.feed:feed)];
    },

    updateFanArt: (state, action: PayloadAction<{fanArt:GalleryInfo; fanArtId:number}>) => {
      state.fanArtList = [...state.fanArtList.map((fanArt)=> fanArt.fanArtId=== action.payload.fanArtId?action.payload.fanArt:fanArt)];
    }

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
      })
      .addCase(getFanArtList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getFanArtList.fulfilled, (state, action) => {
        state.status = 'success';
        state.fanArtList = [...state.fanArtList, ...action.payload.content];
        state.page += 1;
        state.hasMore = !action.payload.last;
      })
      .addCase(getFanArtList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(toggleFanArtLike.fulfilled, (state, action: PayloadAction<number>) => {
        const fanArtId = action.payload;
        const fanArt = state.fanArtList.find(fanArt => fanArt.fanArtId === fanArtId);
        if (fanArt) {
          fanArt.isLike = !fanArt.isLike;
          fanArt.isLike ? fanArt.likeCnt++ : fanArt.likeCnt--;
        }
      })
      .addCase(toggleFanArtLike.rejected, (state, action) => {
        state.error = action.payload as string;
      })
  },
});

export const { resetState, setFilterCondition, setSortCondition, searchFeed, updateFeed, updateFanArt } =
  communityDetailSlice.actions;
export default communityDetailSlice.reducer;
