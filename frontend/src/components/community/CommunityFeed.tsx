import React, { useState } from 'react';
import { ReactComponent as Heart } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as Dots } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as Comment } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as FullHeart } from '../../assets/images/Feed/fullheart.svg';
import { ReactComponent as RightBtn } from '../../assets/images/right.svg';
import { ReactComponent as LeftBtn } from '../../assets/images/left.svg';
import { FeedInfo } from '../../interface/feed';
import { useAppDispatch } from '../../store/hooks/hook';
import { resetState, toggleFeedLike, updateFeed } from '../../features/communityDetail/communityDetailSlice';
import UserProfile from '../common/UserProfile';
import { getUserId, IMAGE_BASE_URL } from '../../apis/core';
import Button from '../common/Button';
import ActionMenu from '../common/ActionMenu';
import CommonModal from '../common/CommonModal';
import { deleteFeedAPI, getFeedDetailAPI } from '../../apis/feed';
import EditFeed from './EditFeed';

interface FeedProps {
  feed: FeedInfo;
  onClick: () => void;
}

export default function CommunityFeed({ feed, onClick }: FeedProps) {
  const {
    feedId,
    userId,
    profileImagePath,
    nickname,
    createdAt,
    likeCnt,
    feedImagePathList,
    content,
    commentCnt,
    tagList,
    isLike,
  } = feed;
  const dispatch = useAppDispatch();
  const [imgIdx, setImgIdx] = useState<number>(0);
  const [animation, setAnimation] = useState<string>('');
  const [showActionMenu, setShowActionMenu] = useState<boolean>(false);
  const [showEditModal, setShowEditModal] = useState<boolean>(false);
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false);
  const currentUserId = getUserId();

  const handleClick = () => {
    onClick();
  };

  const loadFeedDetail = async () => {
    try {
      const response = await getFeedDetailAPI(feedId);
      dispatch(updateFeed({feedId, feed:response}));
    } catch (error) {
      console.error('Error fetching feed details:', error);
    }
  }

  const onDelete = async () => {
      try {
        await deleteFeedAPI(feedId);
        alert('성공적으로 삭제했습니다.');
        dispatch(resetState())
      } catch (error: any) {
        if (error.response && error.response.data) {
          alert(error.response.data);
          return;
        } else {
          alert('삭제에 실패했습니다.');
        }
      } finally {
        setShowDeleteModal(false);
      }
  }

  return (
    <div className="feed" onClick={handleClick}>
      <div className="upper">
        <UserProfile
          userId={userId}
          userNickname={nickname}
          date={new Date(createdAt).toLocaleString()}
          userProfilePath={profileImagePath ? IMAGE_BASE_URL + profileImagePath : null}
        />
        { userId === currentUserId &&
        <div className="dots_box">
          <Dots className="dots hoverup" onClick={e => {
            e.stopPropagation();
            setShowActionMenu(true);
          }} />
          {showActionMenu && (
              <ActionMenu
                onClose={() => setShowActionMenu(false)}
                onEditAction={()=> setShowEditModal(true)}
                onDeleteAction={()=> setShowDeleteModal(true)}
              />
            )}
        </div>
        }
      </div>
      {feedImagePathList.length > 0 && (
        <div className="feed_image_box">
          <div className="slider">
            <div onClick={e => e.stopPropagation()}>
              <Button
                className={`square empty ${imgIdx <= 0 ? 'hidden ' : ''}`}
                onClick={() => {
                  setAnimation('slideRight');
                  setImgIdx(prev => prev - 1);
                }}
              >
                <LeftBtn />
              </Button>
            </div>
            <div className="main_img_container">
              {imgIdx > 0 && (
                <img
                  key={`prev-${imgIdx}`}
                  className={`prev_img ${animation}`}
                  src={IMAGE_BASE_URL + feedImagePathList[imgIdx - 1]}
                  alt="이미지 없음"
                />
              )}
              <img
                key={`main-${imgIdx}`}
                className={`main_img ${animation}`}
                src={IMAGE_BASE_URL + feedImagePathList[imgIdx]}
                alt="이미지 없음"
              />
              {imgIdx < feedImagePathList.length - 1 && (
                <img
                  key={`next-${imgIdx}`}
                  className={`next_img ${animation}`}
                  src={IMAGE_BASE_URL + feedImagePathList[imgIdx + 1]}
                  alt="이미지 없음"
                />
              )}
              <div className="image-counter">
                {imgIdx + 1}/{feedImagePathList.length}
              </div>
            </div>
            <div onClick={e => e.stopPropagation()}>
              <Button
                className={`square empty ${imgIdx >= feedImagePathList.length - 1 ? 'hidden ' : ''}`}
                onClick={() => {
                  setAnimation('slideLeft');
                  setImgIdx(prev => prev + 1);
                }}
              >
                <RightBtn />
              </Button>
            </div>
          </div>
          <div className="pagination-dots" onClick={e => e.stopPropagation()}>
            {feedImagePathList.map((_, idx) => (
              <div
                key={idx}
                className={`pagination-dot ${idx === imgIdx ? 'active' : ''}`}
                onClick={() => {
                  setAnimation('');
                  setImgIdx(idx);
                }}
              ></div>
            ))}
          </div>
        </div>
      )}
      <div className="text">{content}</div>
      <div className="tag">
        {tagList.map((tag, index) => (
          <div key={index}>#{tag}</div>
        ))}
      </div>
      <div className="feed_heart_box" onClick={e => e.stopPropagation()}>
        {likeCnt}
        {!isLike ? (
          <Heart
            className="hoverup"
            onClick={() => {
              dispatch(toggleFeedLike(feedId));
            }}
          />
        ) : (
          <FullHeart
            className="hoverup"
            onClick={() => {
              dispatch(toggleFeedLike(feedId));
            }}
          />
        )}
      </div>
      <div className="feed_comment_box">
        {' '}
        {commentCnt}
        <Comment className='hoverup' />
      </div>
      {//수정모달
        showEditModal && (
          <div className="modal-overlay" style={{zIndex:1100}} onClick = {(e)=>e.stopPropagation()}>
            <div className="feed-edit-modal" >
              <div className="modal-content">
                <div className="modal-header">
                  <div className="modal-header-title">
                    <h2>글 수정</h2>
                  </div>
                  <span className="close" onClick={()=>setShowEditModal(false)}>
                    &times;
                  </span>
                </div>
                <div className="modal-body">
                  <EditFeed
                    onClose={()=>{loadFeedDetail(); setShowEditModal(false);}}
                    feedId={feedId}
                    initialContent={content}
                    initialTags={tagList}
                    initialImages={feedImagePathList}
                  />
                </div>
              </div>
            </div>
          </div>
        )
      }

      {//삭제모달
      showDeleteModal && (
        <CommonModal
          title="삭제 확인"
          msg="정말로 삭제하시겠습니까?"
          onClose={() => setShowDeleteModal(false)}
          onConfirm={onDelete}
        />
      )}
    </div>
  );
}
