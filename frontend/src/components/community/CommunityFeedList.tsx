import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import CommunityFeed from './CommunityFeed';
import { useAppDispatch } from '../../store/hooks/hook';
import { getFeedList, resetState } from '../../features/feed/communityFeedSlice';

export default function CommunityFeedList({idolGroupId}:{idolGroupId:number}) {
  const { feedList, hasMore, status, keyword } = useAppSelector(state => state.communityFeed);
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
          dispatch(getFeedList(idolGroupId));
        }
      });

      if (node) {
        observer.current.observe(node);
      }
    },
    [hasMore, dispatch, status, idolGroupId],
  );

  return (
    <div className="feedlist">
      {status==='loading'||feedList.length>0?feedList.map((feed, index) => (
        <CommunityFeed key={index} feed={feed} />
      )):keyword?<div className='text-center text-xl'>'{keyword}'에 해당하는 피드를 찾을 수 없습니다.</div>
                :<div className='text-center text-xl'>작성된 피드가 없습니다.</div>}
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
