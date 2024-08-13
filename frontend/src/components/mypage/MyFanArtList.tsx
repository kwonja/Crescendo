import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { useAppDispatch } from '../../store/hooks/hook';
import { getMyFanArtList, resetState } from '../../features/mypage/myFeedSlice';
import MyFanart from './MyFanart';

interface MyFanArtListProps {
  userId: number;
}

export default function MyFanartList({ userId }: MyFanArtListProps) {
  const { myFanArtList, hasMore, status, } = useAppSelector(state => state.myFeed);
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
          dispatch(getMyFanArtList(userId));
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
      {status === 'loading' || myFanArtList.length > 0 ? (
        myFanArtList.map((fanArt) => (
          <MyFanart key={fanArt.fanArtId} fanArt={fanArt} />
        ))
      ) : (
        <div className="text-center text-xl">작성한 팬아트가 없습니다.</div>
      )
      }
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
