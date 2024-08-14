import React, { useRef } from 'react';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { ReactComponent as Play } from '../../assets/images/challenge/playbtn.svg';
import { ChallengeDetails } from '../../interface/challenge';
import { IMAGE_BASE_URL } from '../../apis/core';
import { useAppDispatch } from '../../store/hooks/hook';
import {
  decrementChallengeLike,
  incrementChallengeLike,
  setSelectedChallengeDetail,
} from '../../features/challenge/challengeDetailSlice';
import { getChallengeLikeAPI } from '../../apis/challenge';

interface ChallengeProps {
  Challenge: ChallengeDetails;
}
export default function ChallengeDetailItem({ Challenge }: ChallengeProps) {
  const { isLike, challengeVideoPath, likeCnt, nickname, challengeJoinId } = Challenge;
  const dispath = useAppDispatch();
  const videoRef = useRef<HTMLVideoElement | null>(null);

  const handleClick = () => {
    dispath(setSelectedChallengeDetail(Challenge));
  };

  const handleNotLike = async () => {
    await getChallengeLikeAPI(challengeJoinId);
    dispath(decrementChallengeLike(challengeJoinId));
  };
  const handleLike = async () => {
    await getChallengeLikeAPI(challengeJoinId);
    dispath(incrementChallengeLike(challengeJoinId));
  };

  return (
    <div className="challengedetailitem">
      <div className="item-box">
        <video src={`${IMAGE_BASE_URL}${challengeVideoPath}`} ref={videoRef} />
        <div className="info">
          <div className="flex flex-row gap-2 mx-3 my-3 text-white">
            {isLike ? (
              <FullHeart className="w-6 h-6" onClick={handleNotLike} />
            ) : (
              <Heart className="w-6 h-6" onClick={handleLike} />
            )}{' '}
            {likeCnt}
          </div>
          <div className="challengeitem_title">{nickname}</div>
        </div>
        <div className="big-play-button">
          <Play className="w-20 h-20" onClick={handleClick} />
        </div>
      </div>
    </div>
  );
}
