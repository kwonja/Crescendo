import React, { useEffect } from 'react';
import VideoPlayer from '../components/challenge/VideoPlayer';
import { ReactComponent as Write } from '../assets/images/write.svg';
import ChallengeModal from '../components/challenge/ChallengeModal';
import { useAppDispatch, useAppSelector } from '../store/hooks/hook';
import { getChallengeList } from '../features/challenge/challengeSlice';
import ChallengeItem from '../components/challenge/ChallengeItem';

export default function Challenge() {
  const dispatch = useAppDispatch();
  const { currentPage, challengeLists } = useAppSelector(state => state.challenge);
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  useEffect(() => {
    dispatch(getChallengeList({ page: currentPage, size: 4, title: '', sortBy: '' }));
  }, [dispatch, currentPage,isModalOpen]);


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
      <div className="challenge-list">
      <div className="title">SHOW YOUR CHALLENGE!!</div>
      <div className="flex flex-wrap gap-10 mx-auto w-9/12 justify-between">
        {challengeLists.map(challenge => (
          <ChallengeItem Challenge={challenge} key={challenge.challengeId} />
        ))}
      </div>
    </div>
      </div>
      <Write className="fixed right-12 bottom-12 cursor-pointer" onClick={handleOpenModal} />
      {isModalOpen ? <ChallengeModal onClose={handleOpenModal} /> : null}
    </div>
  );
}
