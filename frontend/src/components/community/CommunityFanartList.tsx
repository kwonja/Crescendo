import React, { useCallback, useEffect, useRef } from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { useAppDispatch } from '../../store/hooks/hook';
import { getFanArtList, resetState } from '../../features/communityDetail/communityDetailSlice';
import CommunityFanart from './CommunityFanart';

interface CommunityFeedListProps {
  idolGroupId: number;
  onFanArtClick: (fanArtId: number) => void;
}

export default function CommunityFanartList({
  idolGroupId,
  onFanArtClick,
}: CommunityFeedListProps) {
  const { fanArtList, hasMore, status, keyword } = useAppSelector(state => state.communityDetail);
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
          dispatch(getFanArtList(idolGroupId));
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
      {status === 'loading' || fanArtList.length > 0 ? (
        fanArtList.map(fanArt => (
          <CommunityFanart
            key={fanArt.fanArtId}
            fanArt={fanArt}
            onClick={() => onFanArtClick(fanArt.fanArtId)}
          />
        ))
      ) : keyword ? (
        <div className="text-center text-xl w-full">
          '{keyword}'에 해당하는 팬아트를 찾을 수 없습니다.
        </div>
      ) : (
        <div className="text-center text-xl w-full">작성된 팬아트가 없습니다.</div>
      )}
      {hasMore && <div ref={loadMoreElementRef}>Load More..</div>}
    </div>
  );
}
