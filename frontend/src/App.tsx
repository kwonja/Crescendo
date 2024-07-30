import React from 'react';
import { Outlet } from 'react-router-dom';
<<<<<<< HEAD
import Header from './components/layout/NotLoginHeader';
=======
import Header from './components/header/LoginHeader';
>>>>>>> c5f5e7f69d46d2e3eb491e5f7dc69c0ae2d31df5

export default function App() {
  return (
    <>
      <Header />
      <Outlet />
    </>
  );
}
