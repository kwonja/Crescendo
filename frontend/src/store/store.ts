import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import communityListReducer from '../features/communityList/communityListSlice';


export const store = configureStore({
  reducer: {
    auth: authReducer,
    communityList: communityListReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
