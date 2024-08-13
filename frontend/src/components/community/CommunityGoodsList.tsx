import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { useAppDispatch } from '../../store/hooks/hook';
import { getGoodsList, resetState } from '../../features/communityDetail/communityDetailSlice';
import CommunityGoods from './CommunityGoods';

interface CommunityFeedListProps {
  idolGroupId: number;
  onGoodsClick: (goodsId: number) => void;
}

export default function CommunityGoodsList({ idolGroupId, onGoodsClick }: CommunityFeedListProps) {
  const { goodsList, hasMore, status, keyword } = useAppSelector(state => state.communityDetail);
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
          dispatch(getGoodsList(idolGroupId));
        }
      });

      if (node) {
        observer.current.observe(node);
      }
    },
    [hasMore, dispatch, status, idolGroupId],
  );

  return (
    <div className="gallerylist">
      {status === 'loading' || goodsList.length > 0 ? (
        goodsList.map((goods) => (
          <CommunityGoods key={goods.goodsId} goods={goods} onClick={() => onGoodsClick(goods.goodsId)} />
        ))
      ) : keyword ? (
        <div className="text-center text-xl">'{keyword}'에 해당하는 굿즈를 찾을 수 없습니다.</div>
      ) : (
        <div className="text-center text-xl">작성된 굿즈가 없습니다.</div>
      )}
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
