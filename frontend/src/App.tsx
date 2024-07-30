import React from 'react';
import { Outlet } from 'react-router-dom';
// import Header from './components/header/NotLoginHeader';
import Header from './components/header/LoginHeader';

export default function App() {
  return (
    <>
      <Header />




      
      <Outlet />
    </>
  );
}
