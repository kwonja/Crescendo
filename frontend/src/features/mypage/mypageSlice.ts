//Ducks 패턴 공부예정

import { PayloadAction, createSlice } from "@reduxjs/toolkit";

interface mypageState{
    feed : string;
}

const inistalState : mypageState = {
    feed :"피드 테스트"
}

const mypageSlice = createSlice({
    name :'mypage',
    initialState : inistalState,
    reducers : {
        updateFeed(state : mypageState, action : PayloadAction<string>) {
            state.feed = action.payload;
        }
    }
})

export const {updateFeed} = mypageSlice.actions;
export default mypageSlice.reducer;
