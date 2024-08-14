import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { useAppDispatch } from '../../store/hooks/hook';
import { getMyGoodsList, resetState } from '../../features/mypage/myFeedSlice';
import MyGoods from './MyGoods';

interface MyGoodsListProps {
  userId: number;
}

export default function MyGoodsList({ userId }: MyGoodsListProps) {
  const { myGoodsList, hasMore, status } = useAppSelector(state => state.myFeed);
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
          dispatch(getMyGoodsList(userId));
        }
      });

      if (node) {
        observer.current.observe(node);
      }
    },
    [hasMore, dispatch, status, userId],
  );

  return (
    <div className="gallerylist">
      {status === 'loading' || myGoodsList.length > 0 ? (
        myGoodsList.map(goods => <MyGoods key={goods.goodsId} goods={goods} />)
      ) : (
        <div className="text-center text-xl w-full">작성된 굿즈가 없습니다.</div>
      )}
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
