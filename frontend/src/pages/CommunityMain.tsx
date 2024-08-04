import CommunityFavoriteList from '../components/community/CommunityFavoriteList';
import CommunityList from '../components/community/CommunityList';
import CommunitySearchInput from '../components/community/CommunitySearchInput';
import { useAppSelector } from '../store/hooks/hook';

export default function CommunityMain() {
  const { isLoggedIn } = useAppSelector(state => state.auth);

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
          <CommunitySearchInput />
        </div>
      </div>
      <div className="communitymain_contents">
        <CommunityList />
      </div>
    </div>
}
