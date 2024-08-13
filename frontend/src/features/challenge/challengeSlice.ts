import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { Challenge } from '../../interface/challenge';
import { getChallengeAPI } from '../../apis/challenge';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';
interface ChallengeProps {
  challengeLists: Challenge[];
  unReadAlarmCount: number;
  status: PromiseStatus;
  error: string | undefined;
  currentPage : number;
  size : number;
  selectedChallenge : Challenge;

}
const inistalState: ChallengeProps = {
  challengeLists: [],
  unReadAlarmCount: 0,
  status: '',
  error: '',
  currentPage: 0,
  size : 10,
  selectedChallenge :{
    challengeId: 0,
    title: '',
    challengeVideoPath: '',
    createdAt: '',
    endAt: '',
    userId: 0,
    nickname: '',
    profilePath: '',
    participants: 0
  }
};

interface APIstate{
  page : number,
  size : number,
  title : string
  sortBy : string
}
export const getChallengeList = createAsyncThunk('challengeSlice/getChallengeList', async ({page,size,title,sortBy} : APIstate) => {
  const response = await getChallengeAPI(page,size,title,sortBy);
  return response;
});

const challengeSlice = createSlice({
  name: 'challenge',
  initialState: inistalState,
  reducers: {
    setSelectedChallenge: (state, action: PayloadAction<Challenge>) => {
      state.selectedChallenge = action.payload;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getChallengeList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getChallengeList.fulfilled, (state, action) => {
        state.status = 'success';
        state.challengeLists = action.payload.content;
      })
      .addCase(getChallengeList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
  },
});

export const { setSelectedChallenge } = challengeSlice.actions;
export default challengeSlice.reducer;
