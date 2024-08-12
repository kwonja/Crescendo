import React from 'react';
import { ReactComponent as Participant } from '../../assets/images/challenge/participant.svg';
import { ReactComponent as Timer } from '../../assets/images/challenge/timer.svg';
import { ReactComponent as Play } from '../../assets/images/challenge/playbtn.svg';
export default function ChallengeItem() {
  return (
    <div className="challengeitem">
      <div className="item-box">
        <video src="Dance.mp4" />
        <div className="info">
          <ul>
            <li>
              <Timer /> 1분30초
            </li>
            <li>
              <Participant /> 300명
            </li>
          </ul>
          <div className="challengeitem_title">뉴진스 안무 커버 댄스방</div>
        </div>
        <div className="big-play-button">
          <Play className="w-20 h-20" />
        </div>
      </div>
    </div>
  );
}
