import React, { useState, useEffect, useRef } from 'react';
import { ReactComponent as UserList } from '../../assets/images/userlist.svg';
import { ReactComponent as User } from '../../assets/images/user.svg';
import { ReactComponent as Alarm } from '../../assets/images/alarm.svg';
import { ReactComponent as Chat } from '../../assets/images/chat.svg';
import { Link, NavLink, useLocation } from 'react-router-dom';
import UserMenu from './UserMenu';
import ChatLayout from '../chat/ChatLayout';
import Chatroom from '../chat/ChatRoom';
import { useAppSelector } from '../../store/hooks/hook';
import SearchUser from '../userlist/SearchUser';
import AlarmLayout from '../alarm/AlarmLayout';

export type ModeState = 'chat' | 'alarm' | 'userlist' | 'user' | '';

export default function LoginHeader() {
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const [userMode, setUserMode] = useState<ModeState>('');
  const menuRef = useRef<HTMLUListElement>(null);
  const location = useLocation();

  const { isSelected } = useAppSelector(state => state.chatroom);
  const handleModeClick = (mode: ModeState) => {
    setUserMode(prevMode => (prevMode === mode ? '' : mode));
  };

  useEffect(() => {
    const menuElement = menuRef.current;
    if (menuElement) {
      const activeLink = menuElement.querySelector('.active') as HTMLElement;
      if (activeLink) {
        const { offsetLeft, offsetWidth } = activeLink;
        setIndicatorStyle({
          left: offsetLeft + (offsetWidth - 80) / 2 + 'px', // Center the indicator
          display: 'block',
        });
      } else {
        setIndicatorStyle({ display: 'none' });
      }
    }
  }, [location]);

  return (
    <div className="header">
      <Link to="/">
        <div className="header_title">CRESCENDO</div>
      </Link>

      <ul className="header_menu" ref={menuRef}>
        <li>
          <NavLink to="/community">커뮤니티</NavLink>
        </li>
        <li>
          <NavLink to="/dance">댄스챌린지</NavLink>
        </li>
        <li>
          <NavLink to="/favorite">전국최애자랑</NavLink>
        </li>
        <li>
          <NavLink to="/game">오락실</NavLink>
        </li>
        <div className="indicator" style={indicatorStyle}></div>
      </ul>

      <div className="header_icon">
        <div
          className={` header_icon_div ${userMode === 'chat' ? 'chat' : ''}`}
          onClick={() => handleModeClick('chat')}
        >
          <Chat />
        </div>
        <div
          className={` header_icon_div ${userMode === 'alarm' ? 'alarm' : ''}`}
          onClick={() => handleModeClick('alarm')}
        >
          <Alarm />
        </div>
        <div
          className={` header_icon_div ${userMode === 'userlist' ? 'userlist' : ''}`}
          onClick={() => handleModeClick('userlist')}
        >
          <UserList />
        </div>
        <div
          className={` header_icon_div ${userMode === 'user' ? 'user' : ''}`}
          onClick={() => handleModeClick('user')}
        >
          <User />
        </div>
        {userMode === 'chat' && isSelected === false && <ChatLayout />}
        {userMode === 'chat' && isSelected === true && <Chatroom />}
        {userMode === 'alarm' && <AlarmLayout />}
        {userMode === 'userlist' && <SearchUser handleMode={setUserMode} />}
        {userMode === 'user' && <UserMenu handleMode={() => setUserMode('')} />}
      </div>
    </div>
  );
}
