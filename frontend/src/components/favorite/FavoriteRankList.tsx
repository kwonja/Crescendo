import React, { useCallback, useEffect, useRef, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { ReactComponent as Dots } from '../../assets/images/Favorite/smalldots.svg';
import { ReactComponent as Heart } from '../../assets/images/Favorite/heart.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Favorite/fullheart.svg';
import { IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';
import { getFavoriteRankList, resetState, toggleIsLike } from '../../features/favorite/favoriteSlice';
import ActionMenu from './ActionMenu';

export default function FavoriteRankList() {
  const { favoriteRankList, status, hasMore } = useAppSelector(state => state.favorite);
  const [showActionMenu, setShowActionMenu] = useState<boolean>(false);
  const [actionMenuPosition, setActionMenuPosition] = useState<{top:number, left:number}>({top:0, left:0});
  const [selectedRankId, setSelectedRankId] = useState<number>(-1);
  const dispatch = useAppDispatch();
  const observer = useRef<IntersectionObserver | null>(null);

  useEffect(() => {
    return () => {
      dispatch(resetState());
      if (observer.current) observer.current.disconnect();
    };
  }, [dispatch]);

  const loadMoreElementRef = useCallback(
    (node: HTMLDivElement | null) => {
      if (observer.current) observer.current.disconnect();

      observer.current = new IntersectionObserver(entries => {
        if (entries[0].isIntersecting && hasMore && (status === 'success' || status === '')) {
          dispatch(getFavoriteRankList());
        }
      });

      if (node) {
        observer.current.observe(node);
      }
    },
    [hasMore, dispatch, status],
  );


  const handleDotsClick = (event:React.MouseEvent<SVGSVGElement, MouseEvent>, id:number) => {
    const { pageX, pageY } = event;
    setSelectedRankId(id)
    setActionMenuPosition({top:pageY, left:pageX})
    setShowActionMenu(true);

  }

  return (
    <div className="favoriteranklist">
      {favoriteRankList.map(rankEntry => (
        <div className="favoriteranklist_card" key={rankEntry.favoriteRankId}>
          <div className="dotsbox">
            <Dots className="hoverup" onClick={(event)=>handleDotsClick(event,rankEntry.favoriteRankId)}/>
          </div>
          <div className="favoriteranklist_card_label text-4xl">
            <div>{rankEntry.idolGroupName}</div>
            <div className="separator mx-3">-</div>
            <div>{rankEntry.idolName}</div>
          </div>
          <img src={IMAGE_BASE_URL + rankEntry.favoriteIdolImagePath} alt={rankEntry.idolName} />
          <div className="favoriteranklist_card_info">
            <UserProfile
              className="favoriteranklist_card_user"
              userId={rankEntry.writerId}
              userNickname={rankEntry.writerNickname}
              userProfilePath={IMAGE_BASE_URL + rankEntry.writerProfilePath}
              date={rankEntry.createdAt ? rankEntry.createdAt.split('T')[0] : ''}
            />
            <div className="heartbox">
              {rankEntry.likeCnt}
              {rankEntry.isLike ? (
                <FullHeart
                  className="hoverup"
                  onClick={() => dispatch(toggleIsLike(rankEntry.favoriteRankId))}
                />
              ) : (
                <Heart
                  className="hoverup"
                  onClick={() => dispatch(toggleIsLike(rankEntry.favoriteRankId))}
                />
              )}
            </div>
          </div>
        </div>
      ))}
      {showActionMenu && <ActionMenu favoriteRankId={selectedRankId} position={actionMenuPosition} onClose={()=>setShowActionMenu(false)}/>}
      {(status ==='success' || status === '') && hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
