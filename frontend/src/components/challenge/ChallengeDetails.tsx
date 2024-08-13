import React, { useEffect } from 'react';
import { useParams,useNavigate } from 'react-router-dom';
import VideoPlayerDetail from './VideoPlayerDetail';
import VideoPlayerOrigin from './VideoPlayerOrigin';
import ChallengeDetailItem from './ChallengeDetailItem';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getChallengeDetails } from '../../features/challenge/challengeDetailSlice';
import { ReactComponent as Write } from '../../assets/images/write.svg';
import { ReactComponent as Back } from '../../assets/images/challenge/back.svg';
import ChallengeDetailModal from './ChallengeDetailModal';
export default function ChallengeDetails() {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const { challengeId } = useParams<{ challengeId: string }>();
  const numericChallengeId = challengeId ? Number(challengeId) : 0;
  const {challengeDetailLists} = useAppSelector( state => state.challengeDetail)
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(getChallengeDetails({page : 0 ,size : 10, nickname : '', sortBy : '', challengeId: numericChallengeId}));
  }, [dispatch,numericChallengeId,isModalOpen]);
  const handleOpenModal = () => {
    setIsModalOpen(prev => !prev);
  };
  useEffect(()=>{
    window.scrollTo(0, 0);
  },[])
  const handleBack = ()=>{
    navigate('/dance');
  }
  return (
    <div className="challengedetail">
      <Back className='w-14 h-14 fixed left-12 top-24 cursor-pointer' onClick={handleBack}/>
      <div className="original">
        <VideoPlayerOrigin challengeId={numericChallengeId}/>
      </div>
      <div className="challenger">
        <VideoPlayerDetail />
      </div>
      <div className="challenge-room">
        {
          challengeDetailLists.map( (challenge)=>(
            <ChallengeDetailItem Challenge={challenge} key={challenge.challengeJoinId}/>
          ))
        }
      </div>
      <Write className="fixed right-12 bottom-12 cursor-pointer" onClick={handleOpenModal} />
      {isModalOpen ? <ChallengeDetailModal onClose={handleOpenModal} challengeId={numericChallengeId}/> : null}
    </div>
  );
}
