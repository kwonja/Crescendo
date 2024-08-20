import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CompatClient } from '@stomp/stompjs';

interface WebSocketState {
  client: CompatClient | null;
  isConnected: boolean;
}

const initialState: WebSocketState = {
  client: null,
  isConnected: false,
};

const webSocketSlice = createSlice({
  name: 'websocket',
  initialState,
  reducers: {
    setClient(state, action: PayloadAction<CompatClient>) {
      state.client = action.payload;
    },
    setConnected(state, action: PayloadAction<boolean>) {
      state.isConnected = action.payload;
    },
    disconnectClient(state) {
      if (state.client) {
        state.client.disconnect();
      }
      state.client = null;
      state.isConnected = false;
    },
  },
});

export const { setClient, setConnected, disconnectClient } = webSocketSlice.actions;

export default webSocketSlice.reducer;
