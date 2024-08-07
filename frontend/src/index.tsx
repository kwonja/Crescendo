import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import { router } from './router/router';
import './scss/main.scss';
import { RouterProvider } from 'react-router-dom';

import { Provider } from 'react-redux';
import { store } from './store/store';
import { unwrapResult } from '@reduxjs/toolkit';
import { refreshToken } from './features/auth/authSlice';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

// 앱 초기화 함수
const initApp = async () => {
  try {
    const resultAction = await store.dispatch(refreshToken());
    const accessToken = unwrapResult(resultAction);
    if (accessToken) {
      // console.log('재발급한 엑세스 토큰 값:', accessToken);
    } else {
      // console.log('유효한 리프레쉬 토큰이 없습니다.');
    }
  } catch (error) {
    // console.error('앱 초기화 시 엑세스 토큰 재발급에 실패했습니다. :', error);
  }
};

// 기존 초기화 로직을 감싸서 엑세스 토큰 초기화 후에 실행되도록 수정
initApp().then(() => {
  root.render(
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>,
  );
});

reportWebVitals();
