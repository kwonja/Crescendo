import React, { useState, useEffect, useRef } from 'react';
import { ReactComponent as UserList } from '../../assets/images/userlist.svg';
import { ReactComponent as User } from '../../assets/images/user.svg';
import { ReactComponent as Alarm } from '../../assets/images/alarm.svg';
import { ReactComponent as Chat } from '../../assets/images/chat.svg';
import { Link, NavLink, useLocation } from 'react-router-dom';
import SettingMenu from './SettingMenu';

export default function LoginHeader() {
  const [indicatorStyle, setIndicatorStyle] = useState<React.CSSProperties>({});
  const [userMode,setUserMode] = useState<boolean>(false);
  const menuRef = useRef<HTMLUListElement>(null);
  const location = useLocation();

  useEffect(() => {
    const menuElement = menuRef.current;
    if (menuElement) {
      const activeLink = menuElement.querySelector('.active') as HTMLElement;
      if (activeLink) {
        const { offsetLeft, offsetWidth } = activeLink;
        setIndicatorStyle({
          left: offsetLeft + (offsetWidth - 80) / 2 + 'px', // Center the indicator
          display : 'block'
        });
      }
      else{
        setIndicatorStyle({display : 'none'})
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
        <div><Chat /></div>
        <div><Alarm /></div>
        <div><UserList /></div>
        <div className={` ${userMode ? 'selected' : ''}`} onClick={() => setUserMode((prev) => !prev)}>
        <User />
        {userMode && <SettingMenu />}
      </div>
      </div>
    </div>
  );
}
