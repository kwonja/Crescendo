import React from 'react';
import { ReactComponent as Back } from '../../assets/images/Chat/back.svg';
import { ReactComponent as Hamburger } from '../../assets/images/Chat/hamburger.svg';
import { ReactComponent as Line } from '../../assets/images/Chat/line.svg';
import { ReactComponent as Clip } from '../../assets/images/Chat/clip.svg';
import { ReactComponent as Submit } from '../../assets/images/Chat/submit.svg';
import MyMessage from './MyMessage';
import OtherMessage from './OtherMessage';
export default function Chatroom() {
  return (
    <div className="chatroom">
      <div className="upper">
        <Back />
        <div>권자몬</div>
        <Hamburger/>
        </div>
        <div className='date'>
          <Line style={{width : 20}}/>
          <div>2024 년 07 월 24 일</div>
          <Line/>
        </div>
        <OtherMessage/>
        <MyMessage/>
        
        <div className="send-container">
        <span>
        <input
          type="text"
          className="search-input"
          placeholder='여기에 입력하세요'
        />
        <div className="send-icon">
          <Clip style={{width : "24px", height : "24px"}}/>
          <Submit style={{width : "24px", height : "24px"}}/>
        </div>
      </span>
    </div>
    </div>
  );
}
