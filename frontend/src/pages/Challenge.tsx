import React from 'react';
import VideoPlayer from '../components/challenge/VideoPlayer';
import ChallengeList from '../components/challenge/ChallengeList';
import { ReactComponent as Write } from '../assets/images/write.svg';
import ChallengeModal from '../components/challenge/ChallengeModal';
export default function Challenge() {
  const [isModalOpen, setIsModalOpen] = React.useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(prev => !prev);
  };

  return (
    <div className="challenge">
      <div className="left-box"></div>
      <div className="left">
        <VideoPlayer />
      </div>
      <div className="right">
        <ChallengeList />
      </div>
      <Write className="fixed right-12 bottom-12 cursor-pointer" onClick={handleOpenModal} />
      {isModalOpen ? <ChallengeModal onClose={handleOpenModal} /> : null}
    </div>
  );
}
