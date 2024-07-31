import React from 'react';
import { Link } from 'react-router-dom';

export default function UserMenu() {
  return (
    <ul className="usermenu">
      <li>
        <Link to="mypage">마이페이지</Link>
      </li>
      <li>설정</li>
      <li>로그아웃</li>
    </ul>
  );
}
