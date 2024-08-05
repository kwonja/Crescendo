import { useState } from 'react';
import SearchInput from '../components/common/SearchInput';
import CommunityFavoriteList from '../components/community/CommunityFavoriteList';
import CommunityList from '../components/community/CommunityList';
import { setKeyword } from '../features/communityList/communityListSlice';
import { useAppSelector } from '../store/hooks/hook';
import { useAppDispatch } from '../store/hooks/hook';

export default function CommunityMain() {
  const { isLoggedIn } = useAppSelector(state => state.auth);
  const dispatch = useAppDispatch();
  const [value, setValue] = useState<string>('');
  dispatch(setKeyword(value));

  return <div className="communitymain">
      {
      isLoggedIn && <>
      <div className="communitymain_contents">
        <div className="communitymain_title">MY 커뮤니티</div>
      </div>
      <CommunityFavoriteList />
      </>
      }
      <div className="communitymain_contents">
        <div className="communitymain_title">ALL 커뮤니티</div>
        <div className="communitymain_searchbar">
          <SearchInput placeholder='커뮤니티 검색' value={value} onChange={(event)=>setValue(event.target.value)} onSearch={()=>{dispatch(setKeyword(value))}} />
        </div>
      </div>
      <div className="communitymain_contents">
        <CommunityList />
      </div>
    </div>
}
