import axios from 'axios';

export const BASE_URL = 'http://i11b108.p.ssafy.io:8000';

const config = {
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
};

// axios 인스턴스 생성
export const api = axios.create(config); // 일반 API 요청용 인스턴스
export const Authapi = axios.create(config); // 인증이 필요한 요청용 인스턴스

// 엑세스 토큰을 저장할 로컬 변수
let accessToken: string | null = null;

// 엑세스 토큰 설정 함수
export const setAccessToken = (token: string | null) => {
  accessToken = token;
  if (token) {
    Authapi.defaults.headers.common.Authorization = `Bearer ${token}`;
  } else {
    delete Authapi.defaults.headers.common.Authorization;
  }
};

// Authapi 인스턴스에 요청 인터셉터 추가
Authapi.interceptors.request.use(
  config => {
    if (accessToken) {
      config.headers.common.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  error => Promise.reject(error),
);

// 응답 인터셉터 설정 (엑세스 토큰 갱신)
Authapi.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;
    // 401 Unauthorized 에러 발생 시 처리
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true; // 재시도를 방지하기 위한 플래그 설정
      try {
        // 리프레시 토큰을 사용하여 새로운 엑세스 토큰 요청
        const response = await axios.post(`${BASE_URL}/api/v1/auth/refresh-token`, {});
        const newAccessToken = response.headers.authorization.split(' ')[1]; // Authorization 헤더에서 새로운 엑세스 토큰 추출
        setAccessToken(newAccessToken); // 새로운 엑세스 토큰을 설정
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return Authapi(originalRequest); // 원래의 요청을 새로운 엑세스 토큰으로 재시도
      } catch (refreshError) {
        // 리프레시 토큰도 만료된 경우
        alert('세션이 만료되었습니다. 다시 로그인해 주세요.');
        window.location.href = '/login'; // 로그인 페이지로 리디렉션
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error); // 다른 에러는 그대로 반환
  },
);
