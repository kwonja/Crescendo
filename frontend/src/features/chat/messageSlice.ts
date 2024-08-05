import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { Message } from '../../interface/chat';
import { messagesAPI } from '../../apis/chat';
import { PromiseStatus } from '../follow/followerSlice';

interface messageProps {
  messageList: Message[];
  status: PromiseStatus;
  currentPage: number;
  error: string | undefined;
}
const inistalState: messageProps = {
  messageList: [],
  status: '',
  error: '',
  currentPage: 0,
};

interface APIState {
  userId: number;
  dmGroupId: number;
}
export const getMessages = createAsyncThunk(
  'messageSlice/getMessages',
  async ({ userId, dmGroupId }: APIState) => {
    const response = await messagesAPI(userId, 0, 10, dmGroupId);
    console.log(response);
    return response;
  },
);

const messageSlice = createSlice({
  name: 'messages',
  initialState: inistalState,
  reducers: {
    setMessage: (state, action: PayloadAction<Message>) => {
      state.messageList = [...state.messageList, action.payload];
      console.log(state.messageList);
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getMessages.pending, state => {
        state.status = 'loading';
      })
      .addCase(getMessages.fulfilled, (state, action) => {
        state.status = 'success';
        console.log(action.payload);
        state.messageList = [...action.payload.content.reverse(), ...state.messageList];
        state.currentPage += 1;
      })
      .addCase(getMessages.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setMessage } = messageSlice.actions;
export default messageSlice.reducer;
