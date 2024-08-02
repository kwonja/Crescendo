import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import { router } from './router/router';
import './scss/main.scss';
import { RouterProvider } from 'react-router-dom';
import axios from 'axios';

import { Provider } from 'react-redux';
import { store } from './store/store';

// refreshToken cookie를 주고받기 위한 설정
axios.defaults.withCredentials = true;

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
// root.render(<RouterProvider router={router} />);

root.render(
  <Provider store={store}>
    <RouterProvider router={router} />
  </Provider>,
);

reportWebVitals();
