import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { Message } from '../../interface/chat';
import { messagesAPI } from '../../apis/chat';
import { PromiseStatus } from '../follow/followerSlice';

interface messageProps {
  messageList: Message[];
  status: PromiseStatus;
  error: string | undefined;
}
const inistalState: messageProps = {
  messageList: [],
  status: '',
  error: '',
};

export const getMessages = createAsyncThunk('messageSlice/getMessages', async () => {
  const response = await messagesAPI(1, 1, 10, 1);
  console.log(response);
  return response;
});

const messageSlice = createSlice({
  name: 'messages',
  initialState: inistalState,
  reducers: {
    setMessage: (state, action: PayloadAction<Message>) => {
      console.log(action.payload);
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
        state.messageList = action.payload.content;
      })
      .addCase(getMessages.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export const { setMessage } = messageSlice.actions;
export default messageSlice.reducer;
