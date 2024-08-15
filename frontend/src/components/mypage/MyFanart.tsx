import React from 'react';
import { ReactComponent as Dots } from '../../assets/images/Gallery/whitedots.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Gallery/whitefullheart.svg';
import { ReactComponent as Heart } from '../../assets/images/Gallery/whiteheart.svg';
import { ReactComponent as Comment } from '../../assets/images/Gallery/whitecomment.svg';
import { MyFanArtInfo } from '../../interface/gallery';
import { getUserId, IMAGE_BASE_URL } from '../../apis/core';
import UserProfile from '../common/UserProfile';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFanArtLike } from '../../features/communityDetail/communityDetailSlice';
import { decrementLike, incrementLike } from '../../features/mypage/myFeedSlice';
import { Link } from 'react-router-dom';

interface FanArtProps {
  fanArt: MyFanArtInfo;
}

export default function MyFanart({ fanArt }: FanArtProps) {
  const {
    fanArtId,
    userId,
    profileImagePath,
    nickname,
    likeCnt,
    fanArtImagePathList,
    commentCnt,
    createdAt,
    title,
    isLike,
    idolGroupId,
    idolGroupName,
  } = fanArt;

  const dispatch = useAppDispatch();
  const currentUserId = getUserId();

  return (
    <div className="gallery">
      <div className="gallery-img-container">
        <img className="gallery-img" src={IMAGE_BASE_URL + fanArtImagePathList[0]} alt="팬아트그림" />
        <div className="community_name">
          <Link to={`/community/${idolGroupId}`}>{idolGroupName}</Link>
        </div>
      </div>
      {userId === currentUserId &&
      <div className="dots_box">
        <Dots className="dots hoverup" />
      </div>
      }
      <div className="title_box">
        <div className="type">팬아트</div>
        <div className="title">{title}</div>
      </div>
      <div className="gallery_info">
        <div className="gallery_profile">
          <UserProfile
            userId={userId}
            userNickname={nickname}
            date={new Date(createdAt).toLocaleString()}
            userProfilePath={profileImagePath ? IMAGE_BASE_URL + profileImagePath : null}
          />
        </div>
        <div className="gallery_comment_box">
          <Comment className="gallery_comment" />
          <div className="gallery_comment_cnt">{commentCnt}</div>
        </div>
        <div className="gallery_heart_box">
          {isLike ? (
            <FullHeart
              className="gallery_heart hoverup"
              onClick={() => {
                dispatch(decrementLike({ id: fanArtId, type: 'fanArt' }));
                dispatch(toggleFanArtLike(fanArtId));
              }}
            />
          ) : (
            <Heart
              className="gallery_heart hoverup"
              onClick={() => {
                dispatch(incrementLike({ id: fanArtId, type: 'fanArt' }));
                dispatch(toggleFanArtLike(fanArtId));
              }}
            />
          )}
          <div className="gallery_heart_cnt">{likeCnt}</div>
        </div>
      </div>
    </div>
  );
}
