import React, { useRef, useState } from 'react';
import { ReactComponent as Participant } from '../../assets/images/challenge/participant.svg';
import { ReactComponent as Timer } from '../../assets/images/challenge/timer.svg';
import { ReactComponent as Play } from '../../assets/images/challenge/playbtn.svg';
import { ReactComponent as Enter } from '../../assets/images/challenge/enter.svg';
import { Challenge } from '../../interface/challenge';
import { IMAGE_BASE_URL } from '../../apis/core';
import { useAppDispatch } from '../../store/hooks/hook';
import { setSelectedChallenge } from '../../features/challenge/challengeSlice';


interface ChallengeProps{
  Challenge : Challenge
}
export default function ChallengeItem({Challenge} : ChallengeProps) {
  const dispath = useAppDispatch();
  const {title,challengeVideoPath,participants} = Challenge;
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const [duration, setDuration] = useState<number>(0);

  const handleLoadedMetadata = () => {
    if (videoRef.current) {
      const videoDuration = videoRef.current?.duration;
      setDuration(videoDuration);
    }
  };

  const handleClick=()=>{
    dispath(setSelectedChallenge(Challenge));
  }
  return (
    <div className="challengeitem">
      <div className="item-box">
        <video src={`${IMAGE_BASE_URL}${challengeVideoPath}`}
          onLoadedMetadata={handleLoadedMetadata}
          ref={videoRef}
        />
        <div className="info">
          <ul>
            <li>
              <Timer /> {`${Math.floor(duration)}초`}
            </li>
            <li>
              <Participant /> {participants}
            </li>
          </ul>
          <Enter className="absolute right-3 top-3" />
          <div className="challengeitem_title">{title}</div>
        </div>
        <div className="big-play-button">
          <Play className="w-20 h-20" onClick={handleClick}/>
        </div>
      </div>
    </div>
  );
}
