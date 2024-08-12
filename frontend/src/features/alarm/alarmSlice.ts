import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { getAlarmAPI, getUnReadAlarmCountAPI } from '../../apis/alarm';
import { Alarm } from '../../interface/alarm';

export type PromiseStatus = 'loading' | 'success' | 'failed' | '';
interface AlarmProps {
  alramList: Alarm[];
  unReadAlarmCount: number;
  status: PromiseStatus;
  error: string | undefined;
}
const inistalState: AlarmProps = {
  alramList: [],
  unReadAlarmCount: 0,
  status: '',
  error: '',
};
export const getAlarmList = createAsyncThunk('alarmSlice/getAlarmList', async () => {
  const response = await getAlarmAPI(0, 10);
  return response;
});

export const getUnReadAlarmCount = createAsyncThunk('alarmSlice/getUnReadAlarmCount', async () => {
  const response = await getUnReadAlarmCountAPI();
  return response;
});

const alarmSlice = createSlice({
  name: 'feed',
  initialState: inistalState,
  reducers: {
    incrementUnRead: state => {
      state.unReadAlarmCount++;
    },
    decrementUnRead: state => {
      state.unReadAlarmCount--;
    },
    deleteAlarm: (state, action: PayloadAction<number>) => {
      state.alramList = state.alramList.filter(alarm => alarm.alarmId !== action.payload);
    },
    readAlarmUpdate: (state, action: PayloadAction<number>) => {
      state.alramList = state.alramList.map(alarm => {
        if (alarm.alarmId === action.payload) {
          return { ...alarm, isRead: true };
        }
        return alarm;
      });
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getAlarmList.pending, state => {
        state.status = 'loading';
      })
      .addCase(getAlarmList.fulfilled, (state, action) => {
        state.status = 'success';
        state.alramList = action.payload.content;
      })
      .addCase(getAlarmList.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(getUnReadAlarmCount.fulfilled, (state, action) => {
        state.unReadAlarmCount = action.payload;
      });
  },
});

export const { incrementUnRead, decrementUnRead, deleteAlarm,readAlarmUpdate } = alarmSlice.actions;
export default alarmSlice.reducer;
