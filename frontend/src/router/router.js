import Main from '../pages/Main';
import CommunityMain from '../pages/CommunityMain';
import App from '../App';
import { createBrowserRouter } from 'react-router-dom';
import ErrorPage from '../components/layout/ErrorPage';
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
    ],
  },
]);
