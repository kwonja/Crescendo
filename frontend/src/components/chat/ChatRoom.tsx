import React from 'react';
import { ReactComponent as Back } from '../../assets/images/Chat/back.svg';
import { ReactComponent as Hamburger } from '../../assets/images/Chat/hamburger.svg';
export default function Chatroom() {
  return (
    <div className="chatroom">
      <div className="upper">
        <Back />
        <div>권자몬</div>
        <Hamburger />
      </div>
    </div>
  );
}
