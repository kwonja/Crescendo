import React from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import CommunityFeed from '../community/CommunityFeed';

export default function MyFeedList() {
  const feedlist = useAppSelector(state => state.myFeed.myFeedList);

  return (
    <div className="feedlist">
      {feedlist.map((feed, index) => (
        <CommunityFeed key={index} feed={feed} onClick={()=>{}}/>
      ))}
    </div>
  );
}
