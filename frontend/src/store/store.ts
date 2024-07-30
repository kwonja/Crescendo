import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import mypageReducer from '../features/mypage/mypageSlice';
export const store = configureStore({
  reducer: {
    auth: authReducer,
    mypage : mypageReducer,
  },
});


export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
