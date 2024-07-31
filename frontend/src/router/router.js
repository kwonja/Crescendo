import Main from '../pages/Main';
import CommunityMain from '../pages/CommunityMain';
import App from '../App';
import { createBrowserRouter } from 'react-router-dom';
import Login from '../pages/Login.tsx';
import Signup from '../pages/Signup.tsx';
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
        path: '/login',
        element: <Login />,
      },
      {
        path: '/signup',
        element: <Signup />,
      },
      {
        path: '/community',
        element: <CommunityMain />,
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
