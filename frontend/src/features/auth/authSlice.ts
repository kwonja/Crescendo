import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { api, setAccessToken } from '../../apis/core';

// 인증 관련 상태 인터페이스 정의
interface AuthState {
  email: string;
  loading: boolean;
  error: string | null;
  isLoggedIn: boolean;
  accessToken: string | null;
}

// 초기 상태 정의
const initialState: AuthState = {
  email: '',
  loading: false,
  error: null,
  isLoggedIn: false,
  accessToken: null,
};

// 로그인 비동기 함수
export const login = createAsyncThunk(
  'auth/login',
  async ({ email, password }: { email: string; password: string }, { rejectWithValue }) => {
    try {
      const response = await api.post('/api/v1/auth/login', { email, password });
      const accessToken = response.headers.Authorization.split(' ')[1]; // Authorization 헤더에서 액세스 토큰 추출

      // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
      // axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`;
      setAccessToken(accessToken);

      return { email, accessToken };
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
      state.accessToken = null;
    },
  },

  // extraReducers : 비동기 액션(주로 createAsyncThunk로 생성된 액션)을 처리하기 위한 특별한 필드
  extraReducers: builder => {
    builder
      // 비동기 액션이 시작될 때의 상태 처리
      .addCase(login.pending, state => {
        state.loading = true; // 로그인 요청 중 상태를 로딩 중으로 설정합니다.
        state.error = null; // 오류 상태를 초기화합니다.
      })
      // 비동기 액션이 성공적으로 완료됐을 때의 상태 처리
      .addCase(
        login.fulfilled,
        (state, action: PayloadAction<{ email: string; accessToken: string }>) => {
          state.loading = false; // 로딩 상태 해제
          state.isLoggedIn = true; // 로그인 상태를 true로 설정
          state.email = action.payload.email; // 서버에서 받은 이메일 저장
          state.accessToken = action.payload.accessToken; // 서버에서 받은 액세스 토큰 저장
          state.accessToken = action.payload.accessToken; // 서버에서 받은 액세스 토큰 저장
        },
      )
      // 비동기 액션이 실패했을 때의 상태 처리
      .addCase(login.rejected, (state, action) => {
        state.loading = false; // 로딩 상태 해제
        state.error = action.payload as string; // 오류 메시지 저장
      });
  },
});

// 액션 및 리듀서 내보내기
export const { reset } = authSlice.actions;
export default authSlice.reducer;
