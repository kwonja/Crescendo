import CommunityFavoriteList from '../components/Community/CommunityFavoriteList';
import CommunityList from '../components/Community/CommunityList';
import CommunitySearchInput from '../components/Community/CommunitySearchInput';

export default function CommunityMainPage() {

  return <div className='communitymain'>
    <div className='communitymain_contents'>
      <div className='communitymain_title'>MY 커뮤니티</div>
    </div>
    <CommunityFavoriteList/>
    <div className='communitymain_contents'>
      <div className='communitymain_title'>ALL 커뮤니티</div>
      <div className='communitymain_searchbar'>
        <CommunitySearchInput />
      </div>
    </div>
    <CommunityList/>
  </div>;
}
