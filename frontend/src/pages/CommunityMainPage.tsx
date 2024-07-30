import CommunityFavoriteList from '../components/Community/CommunityFavoriteList';
import CommunityList from '../components/Community/CommunityList';

export default function CommunityMainPage() {

  return <div className='communitymain'>
    <div className='communitymain_title'>MY 커뮤니티</div>
    <CommunityFavoriteList/>
    <div className='communitymain_title'>ALL 커뮤니티</div>
    <CommunityList/>
  </div>;
}
