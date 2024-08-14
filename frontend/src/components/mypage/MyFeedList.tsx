import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { useAppDispatch } from '../../store/hooks/hook';
import MyFeed from './MyFeed';
import { getMyFeedList, resetState } from '../../features/mypage/myFeedSlice';

interface MyFeedListProps {
  userId: number;
}

export default function MyFeedList({ userId }: MyFeedListProps) {
  const { myFeedList, hasMore, status } = useAppSelector(state => state.myFeed);
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
          dispatch(getMyFeedList(userId));
        }
      });

      if (node) {
        observer.current.observe(node);
      }
    },
    [hasMore, dispatch, status, userId],
  );

  return (
    <div className="feedlist">
      {status === 'loading' || myFeedList.length > 0 ? (
        myFeedList.map(feed => <MyFeed key={feed.feedId} feed={feed} />)
      ) : (
        <div className="text-center text-xl w-full">작성한 피드가 없습니다.</div>
      )}
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
