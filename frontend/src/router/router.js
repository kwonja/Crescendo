import Main from '../pages/Main';
import App from '../App';
import { createBrowserRouter } from 'react-router-dom';
import ErrorPage from '../components/error/ErrorPage';
import MyPage from '../pages/MyPage';
export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: '/',
        element: <Main />,
      },
      {
        path: '/favorite',
        element: <Main />,
      },
      {
        path: '/dance',
        element: <Main />,
      },
      {
        path: '/game',
        element: <Main />,
      },
      {
        path: '/mypage',
        element: <MyPage />,
      },
    ],
  },
]);
