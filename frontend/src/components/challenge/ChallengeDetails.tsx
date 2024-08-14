import React, { useCallback, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import VideoPlayerDetail from './VideoPlayerDetail';
import VideoPlayerOrigin from './VideoPlayerOrigin';
import ChallengeDetailItem from './ChallengeDetailItem';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import {
  getChallengeDetails,
  setChallengeDetailPage,
  setSelectedChallengeDetail,
} from '../../features/challenge/challengeDetailSlice';
import { ReactComponent as Write } from '../../assets/images/write.svg';
import { ReactComponent as Back } from '../../assets/images/challenge/back.svg';
import ChallengeDetailModal from './ChallengeDetailModal';
export default function ChallengeDetails() {
  const navigate = useNavigate();
  const loader = useRef<HTMLDivElement | null>(null);
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const { challengeId } = useParams<{ challengeId: string }>();
  const numericChallengeId = challengeId ? Number(challengeId) : 0;
  const { challengeDetailLists, currentPage } = useAppSelector(state => state.challengeDetail);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(
      getChallengeDetails({
        page: 0,
        size: 10,
        nickname: '',
        sortBy: '',
        challengeId: numericChallengeId,
      }),
    );
  }, [dispatch, numericChallengeId, currentPage]);
  const handleOpenModal = () => {
    setIsModalOpen(prev => !prev);
  };
  useEffect(() => {
    window.scrollTo(0, 0);

    return () => {
      dispatch(
        setSelectedChallengeDetail({
          challengeJoinId: 0,
          challengeVideoPath: '',
          isLike: false,
          likeCnt: 0,
          nickname: '',
          score: 0,
          userId: 0,
        }),
      );
    };
  }, [dispatch]);
  const handleBack = () => {
    navigate('/dance');
  };

  const handleObserver = useCallback(
    (entries: IntersectionObserverEntry[]) => {
      const target = entries[0];
      if (target.isIntersecting) {
        dispatch(setChallengeDetailPage());
      }
    },
    [dispatch],
  );

  useEffect(() => {
    const option = {
      threshold: 0.1,
    };
    const observer = new IntersectionObserver(handleObserver, option);
    if (loader.current) observer.observe(loader.current);
  }, [handleObserver]);
  return (
    <div className="challengedetail">
      <Back className="w-14 h-14 fixed left-12 top-24 cursor-pointer" onClick={handleBack} />
      <div className="original">
        <VideoPlayerOrigin challengeId={numericChallengeId} />
      </div>
      <div className="challenger">
        <VideoPlayerDetail />
      </div>
      <div className="challenge-room">
        <div className="title">챌린지 목록</div>

        {
         challengeDetailLists.length ===0 ? ( <div className='flex flex-col gap-5'><div className='flex w-full text-4xl'>아직 등록된 챌린지가 없습니다</div> <div className='flex w-full justify-center text-4xl'>지금 참여하세요😀</div></div>) : (
          challengeDetailLists.map(challenge => (
            <ChallengeDetailItem Challenge={challenge} key={challenge.challengeJoinId} />
          ))
         )  
        }
        <div className="w-4 h-4" ref={loader}></div>
      </div>
      <Write className="fixed right-12 bottom-12 cursor-pointer" onClick={handleOpenModal} />
      {isModalOpen ? (
        <ChallengeDetailModal onClose={handleOpenModal} challengeId={numericChallengeId} />
      ) : null}
    </div>
  );
}
