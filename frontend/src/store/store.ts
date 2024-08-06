import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import feedReducer from '../features/feed/feedSlice';
import communityListReducer from '../features/communityList/communityListSlice';
import followerReducer from '../features/follow/followerSlice';
import followingReducer from '../features/follow/followingSlice';
import chatroomReducer from '../features/chat/chatroomSlice';
import messagesReducer from '../features/chat/messageSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    feed: feedReducer,
    communityList: communityListReducer,
    follower: followerReducer,
    following: followingReducer,
    chatroom: chatroomReducer,
    message: messagesReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
