import React from 'react';
import { Link } from 'react-router-dom';

interface MenuProps{
  handleMode? : () => void;
}
export default function UserMenu({handleMode} : MenuProps) {
  return (
    <ul className="usermenu">
      <li>
        <Link to="mypage" onClick={handleMode}>마이페이지</Link>
      </li>
      <li>설정</li>
      <li>로그아웃</li>
    </ul>
  );
}
