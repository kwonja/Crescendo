import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../../store/store';
import { logoutUser } from '../../features/auth/authSlice';

interface MenuProps {
  handleMode?: () => void;
}

export default function UserMenu({ handleMode }: MenuProps) {
  const navigate = useNavigate();
  const dispatch: AppDispatch = useDispatch();

  const handleLogout = () => {
    dispatch(logoutUser());
    navigate('/');
  };

  return (
    <ul className="usermenu">
      <li>
        <Link to="mypage" onClick={handleMode}>
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
