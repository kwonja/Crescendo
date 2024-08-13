import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import VideoPlayerDetail from './VideoPlayerDetail';
import { getChallengeDetailsAPI, getChallengeOriginAPI } from '../../apis/challenge';

export default function ChallengeDetails() {
  const { challengeId } = useParams<{ challengeId: string }>();
  const [origin, setOrigin] = useState<string>();

  useEffect(() => {
    const getChallengeDetails = async () => {
      const participantResponse = await getChallengeDetailsAPI(0, 10, '', '', Number(challengeId));
      console.log(participantResponse);

      const originResponse = await getChallengeOriginAPI(Number(challengeId));
      setOrigin(originResponse);
      console.log(origin)
    };
    getChallengeDetails();
  }, [challengeId,origin]);

  return (
    <div className="challengedetail">
      <div className="original">
        <VideoPlayerDetail />
      </div>
      <div className="challenger">
        <VideoPlayerDetail />
      </div>
      <div className="challenge-room"></div>
    </div>
  );
}
