import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
<<<<<<< HEAD
import communityListReducer from '../features/communityList/communityListSlice';

=======
import mypageReducer from '../features/mypage/mypageSlice';
>>>>>>> f6c3cf09550e2ae03d605a43f6e40cac75dd7db5

export const store = configureStore({
  reducer: {
    auth: authReducer,
<<<<<<< HEAD
    communityList: communityListReducer,
=======
    mypage: mypageReducer,
>>>>>>> f6c3cf09550e2ae03d605a43f6e40cac75dd7db5
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
