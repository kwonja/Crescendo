import React from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import { ReactComponent as Dots} from '../../assets/images/Favorite/smalldots.svg'
import { ReactComponent as Heart} from '../../assets/images/Favorite/heart.svg'
import { ReactComponent as FullHeart} from '../../assets/images/Favorite/fullheart.svg'
import UserProfile from '../common/UserProfile';

export default function FavoriteRankList() {
  const favoriteRankList = useAppSelector(state => state.favorite).favoriteRankList;
  const idolName = "민지";
  const idolGroupName = "NewJeans"

  return (
    <div className="favoriteranklist">
      {favoriteRankList.map((rankEntry) => (
        <div className='favoriteranklist_card' key= {rankEntry.favoriteRankId}> 
          <div className='dotsbox'><Dots className='hoverup' /></div>
          <div className='favoriteranklist_card_label text-4xl'>
            <div>{idolGroupName}</div>
            <div className='separator mx-3'>-</div>
            <div>{idolName}</div>
          </div>
          <img src={rankEntry.favoriteIdolImagePath} alt={idolName} />
          <div className='favoriteranklist_card_info'>
            <UserProfile
              className='favoriteranklist_card_user'
              userId={rankEntry.writerId} 
              userNickname={rankEntry.writerNickname} 
              userProfilePath={rankEntry.writerProfilePath} 
              date={rankEntry.createdAt.split('T')[0]}
            />
            <div className='heartbox'>
              {rankEntry.likeCnt}
              {rankEntry.isLike?<FullHeart className='hoverup' />:<Heart className='hoverup' />}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
