import React, { useCallback, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { ReactComponent as Dots } from '../../assets/images/Favorite/smalldots.svg';
import { ReactComponent as Heart } from '../../assets/images/Favorite/heart.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Favorite/fullheart.svg';
import { IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';
import { getFavoriteRankList, resetState, toggleIsLike } from '../../features/favorite/favoriteSlice';

export default function FavoriteRankList() {
  const { favoriteRankList, status, hasMore } = useAppSelector(state => state.favorite);
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

  return (
    <div className="favoriteranklist">
      {favoriteRankList.map(rankEntry => (
        <div className="favoriteranklist_card" key={rankEntry.favoriteRankId}>
          <div className="dotsbox">
            <Dots className="hoverup" />
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
      {(status ==='success' || status === '') && hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
