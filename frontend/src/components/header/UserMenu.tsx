import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../../store/store';
import { logoutUser } from '../../features/auth/authSlice';
import { getUserId } from '../../apis/core';

interface MenuProps {
  handleMode?: () => void;
}

export default function UserMenu({ handleMode }: MenuProps) {
  const dispatch: AppDispatch = useDispatch();

  const handleLogout = () => {
    dispatch(logoutUser());
  };

  return (
    <ul className="usermenu">
      <li>
        <Link to={`mypage/${getUserId()}`} onClick={handleMode}>
          마이페이지
        </Link>
      </li>
      <li>설정</li>
      <li className="logout-button" onClick={handleLogout}>
        로그아웃
      </li>
    </ul>
  );
}
