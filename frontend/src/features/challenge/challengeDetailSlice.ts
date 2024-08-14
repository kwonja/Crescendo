import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { getChallengeDetailsAPI } from '../../apis/challenge';
import { ChallengeDetails } from '../../interface/challenge';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';
interface ChallengeDetailProps {
  challengeDetailLists: ChallengeDetails[];
  status: PromiseStatus;
  error: string | undefined;
  currentPage: number;
  size: number;
  selectedChallengeDetail: ChallengeDetails;
}
const inistalState: ChallengeDetailProps = {
  challengeDetailLists: [],
  status: '',
  error: '',
  currentPage: 0,
  size: 10,
  selectedChallengeDetail: {
    challengeJoinId: 0,
    challengeVideoPath: '',
    isLike: false,
    likeCnt: 0,
    nickname: '',
    score: 0,
    userId: 0,
  },
};

interface APIstate {
  page: number;
  size: number;
  nickname: string;
  sortBy: string;
  challengeId: number;
}

export const getChallengeDetails = createAsyncThunk(
  'challengeDetailSlice/getChallengeList',
  async ({ page, size, nickname, sortBy, challengeId }: APIstate) => {
    const response = await getChallengeDetailsAPI(page, size, nickname, sortBy, challengeId);
    return response;
  },
);

const challengeDetailSlice = createSlice({
  name: 'challengeDetail',
  initialState: inistalState,
  reducers: {
    setSelectedChallengeDetail: (state, action: PayloadAction<ChallengeDetails>) => {
      state.selectedChallengeDetail = action.payload;
    },
    decrementChallengeLike: (state, action: PayloadAction<number>) => {
      const challengeDetail = state.challengeDetailLists.find(
        detail => detail.challengeJoinId === action.payload,
      );
      if (challengeDetail) {
        challengeDetail.isLike = false;
        challengeDetail.likeCnt--;
      }
    },
    incrementChallengeLike: (state, action: PayloadAction<number>) => {
      const challengeDetail = state.challengeDetailLists.find(
        detail => detail.challengeJoinId === action.payload,
      );
      if (challengeDetail) {
        challengeDetail.isLike = true;
        challengeDetail.likeCnt++;
      }
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getChallengeDetails.pending, state => {
        state.status = 'loading';
      })
      .addCase(getChallengeDetails.fulfilled, (state, action) => {
        state.status = 'success';
        state.challengeDetailLists = action.payload.content;
      })
      .addCase(getChallengeDetails.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setSelectedChallengeDetail, decrementChallengeLike, incrementChallengeLike } =
  challengeDetailSlice.actions;
export default challengeDetailSlice.reducer;
