import React from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { ReactComponent as Dots} from '../../assets/images/Favorite/smalldots.svg'
import { ReactComponent as Heart} from '../../assets/images/Favorite/heart.svg'
import { ReactComponent as FullHeart} from '../../assets/images/Favorite/fullheart.svg'
import { IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';
import { toggleIsLike } from '../../features/favorite/favoriteSlice';

export default function FavoriteRankList() {
  const favoriteRankList = useAppSelector(state => state.favorite).favoriteRankList;
  const idolName = "미연";
  const idolGroupName = "(여자)아이들"
  const dispatch = useAppDispatch();

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
          <img src={IMAGE_BASE_URL+rankEntry.favoriteIdolImagePath} alt={idolName} />
          <div className='favoriteranklist_card_info'>
            <UserProfile
              className='favoriteranklist_card_user'
              userId={rankEntry.writerId} 
              userNickname={rankEntry.writerNickname} 
              userProfilePath={IMAGE_BASE_URL+rankEntry.writerProfilePath} 
              date={rankEntry.createdAt?rankEntry.createdAt.split('T')[0]:""}
            />
            <div className='heartbox'>
              {rankEntry.likeCnt}
              {rankEntry.isLike?<FullHeart className='hoverup' onClick={()=>dispatch(toggleIsLike(rankEntry.favoriteRankId))}/>:<Heart className='hoverup' onClick={()=>dispatch(toggleIsLike(rankEntry.favoriteRankId))} />}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
