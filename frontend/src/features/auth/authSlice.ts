import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { api } from '../../apis/core';

// 인증 관련 상태 인터페이스 정의
interface AuthState {
  email: string;
  loading: boolean;
  error: string | null;
  isLoggedIn: boolean;
}

// 초기 상태 정의
const initialState: AuthState = {
  email: '',
  loading: false,
  error: null,
  isLoggedIn: false,
};

// 로그인 비동기 함수
export const login = createAsyncThunk(
  'auth/login',
  async ({ email, password }: { email: string; password: string }, { rejectWithValue }) => {
    try {
      const response = await api.post('/api/v1/auth/login', {
        email,
        password,
      });
      return response.data;
    } catch (error) {
      return rejectWithValue('Login failed');
    }
  },
);

// 인증 슬라이스 생성
const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    reset(state) {
      state.email = '';
      state.loading = false;
      state.error = null;
      state.isLoggedIn = false;
    },
  },
  extraReducers: builder => {
    builder
      .addCase(login.pending, state => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action: PayloadAction<string>) => {
        state.loading = false;
        state.isLoggedIn = true;
        state.email = action.payload;
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

// 액션 및 리듀서 내보내기
export const { reset } = authSlice.actions;
export default authSlice.reducer;
