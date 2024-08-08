import React from 'react';
import Feed from '../common/Feed';
import { useAppSelector } from '../../store/hooks/hook';

export default function CommunityFeedList() {
  const feedlist = useAppSelector(state => state.feed.myFeedList);

  return (
    <div className="feedlist">
      {feedlist.map((feed, index) => (
        <Feed key={index} feed={feed} />
      ))}
    </div>
  );
}
