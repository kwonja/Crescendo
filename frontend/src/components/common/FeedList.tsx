import React from 'react';
import Feed from './Feed';
import { useAppSelector } from '../../store/hooks/hook';

export default function FeedList() {
  const feedlist = useAppSelector(state => state.feed.myFeedList);

  return (
    <div className="feedlist">
      {feedlist.map((feed, index) => (
        <Feed key={index} feed={feed} />
      ))}
    </div>
  );
}
