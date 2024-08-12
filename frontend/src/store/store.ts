import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import feedReducer from '../features/feed/feedSlice';
import communityListReducer from '../features/communityList/communityListSlice';
import followerReducer from '../features/mypage/followerSlice';
import followingReducer from '../features/mypage/followingSlice';
import chatroomReducer from '../features/chat/chatroomSlice';
import messagesReducer from '../features/chat/messageSlice';
import favoriteReducer from '../features/favorite/favoriteSlice';
import alarmReducer from '../features/alarm/alarmSlice';
import communityFeedReducer from '../features/communityDetail/communityDetailSlice';
import commentReducer from '../features/comment/commentSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    feed: feedReducer,
    communityList: communityListReducer,
    follower: followerReducer,
    following: followingReducer,
    chatroom: chatroomReducer,
    message: messagesReducer,
    favorite: favoriteReducer,
    alarm: alarmReducer,
    communityFeed: communityFeedReducer,
    comment: commentReducer
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
