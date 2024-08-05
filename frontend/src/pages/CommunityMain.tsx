import CommunityFavoriteList from '../components/community/CommunityFavoriteList';
import CommunityList from '../components/community/CommunityList';
import CommunitySearchInput from '../components/community/CommunitySearchInput';

export default function CommunityMain() {
  return (
    <div className="communitymain">
      <div className="communitymain_contents">
        <div className="communitymain_title">MY 커뮤니티</div>
      </div>
      <CommunityFavoriteList />
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
  );
}
