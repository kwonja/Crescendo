import React from 'react';
// import { useAppSelector } from '../../store/hooks/hook';
import CommunityFeed from './CommunityFeed';
import { FeedInfo } from '../../interface/feed';

export default function CommunityFeedList() {
  // const feedlist = useAppSelector(state => state.communityFeed.feedList);
  const feedList:FeedInfo[] = []

  return (
    <div className="feedlist">
      {feedList.map((feed, index) => (
        <CommunityFeed key={index} feed={feed} />
      ))}
    </div>
  );
}
