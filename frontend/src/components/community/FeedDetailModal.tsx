import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, Authapi, getUserId } from '../../apis/core';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFeedLike } from '../../features/feed/communityFeedSlice';
import { ReactComponent as HeartIcon } from '../../assets/images/Feed/white_heart.svg';
import { ReactComponent as FullHeartIcon } from '../../assets/images/Feed/white_fullheart.svg';
import { ReactComponent as MenuIcon } from '../../assets/images/Feed/white_dots.svg';
import { ReactComponent as NextButton } from '../../assets/images/Feed/next_button.svg';
import { ReactComponent as PrevButton } from '../../assets/images/Feed/prev_button.svg';
import { ReactComponent as UserProfileImageDefault } from '../../assets/images/UserProfile/reduser.svg';
import FeedDetailMenu from './DropdownMenu';
import EditFeed from './EditFeed';
import '../../scss/components/community/_feeddetailmodal.scss';

type FeedDetailResponse = {
  userId: number;
  profileImagePath: string;
  nickname: string;
  createdAt: string;
  lastModified?: string;
  likeCnt: number;
  isLike?: boolean;
  feedImagePathList: string[];
  content: string;
  commentCnt: number;
  tagList: string[];
};

type Comment = {
  feedCommentId: number;
  userId: number;
  nickname: string;
  profileImagePath: string | null;
  content: string;
  createdAt: string;
};

type FeedDetailModalProps = {
  show: boolean;
  onClose: () => void;
  feedId: number;
};

const FeedDetailModal: React.FC<FeedDetailModalProps> = ({ show, onClose, feedId }) => {
  const [feedDetail, setFeedDetail] = useState<FeedDetailResponse | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState('');
  const [activeImageIndex, setActiveImageIndex] = useState(0);
  const [menuVisible, setMenuVisible] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const commentsRef = useRef<HTMLDivElement | null>(null);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const currentUserId = getUserId();

  const loadFeedDetail = useCallback(async () => {
    try {
      const response = await Authapi.get(`/api/v1/community/feed/${feedId}`);
      setFeedDetail(response.data);
    } catch (error) {
      console.error('Error fetching feed details:', error);
    }
  },[feedId]);

  const loadComments = useCallback(async () => {
    try {
      const response = await api.get(`/api/v1/community/feed/${feedId}/comment`, {
        params: { page: 0, size: 5 },
      });
      setComments(response.data.content);
    } catch (error) {
      console.error('Error fetching comments:', error);
    }
  },[feedId]);

  useEffect(() => {
    if (show) {
      setComments([]);
      loadFeedDetail();
      loadComments();
    }
  }, [show, feedId,loadComments,loadFeedDetail]);

  const handlePrevImage = () => {
    setActiveImageIndex(prevIndex =>
      prevIndex > 0 ? prevIndex - 1 : feedDetail!.feedImagePathList.length - 1,
    );
  };

  const handleNextImage = () => {
    setActiveImageIndex(prevIndex =>
      prevIndex < feedDetail!.feedImagePathList.length - 1 ? prevIndex + 1 : 0,
    );
  };

  const handleAddComment = () => {
    if (newComment.trim() === '') return;

    const formData = new FormData();
    formData.append('content', newComment);

    Authapi.post(`/api/v1/community/feed/${feedId}/comment`, formData)
      .then(() => {
        setNewComment('');
        loadComments();
      })
      .catch(error => {
        console.error('댓글 작성 오류:', error);
      });
  };

  const handleLikeToggle = () => {
    if (feedDetail) {
      dispatch(toggleFeedLike(feedId));
      setFeedDetail(prevDetail =>
        prevDetail
          ? {
              ...prevDetail,
              isLike: !prevDetail.isLike,
              likeCnt: prevDetail.isLike ? prevDetail.likeCnt - 1 : prevDetail.likeCnt + 1,
            }
          : prevDetail,
      );
    }
  };

  const handleMenuToggle = () => {
    setMenuVisible(prevVisible => !prevVisible);
  };

  const handleEdit = () => {
    setMenuVisible(false);
    setEditModalVisible(true);
  };

  const handleEditModalClose = async () => {
    await loadFeedDetail();
    setEditModalVisible(false);
  };

  // 피드 삭제
  const handleDelete = async () => {
    setMenuVisible(false);
    const confirmDelete = window.confirm('삭제 후에는 복구가 불가능합니다. 정말 삭제하시겠습니까?');
    if (confirmDelete) {
      try {
        await Authapi.delete(`/api/v1/community/feed/${feedId}`);
        alert('삭제되었습니다.');
        navigate(0);
        onClose();
      } catch (error) {
        console.error('피드 삭제 오류:', error);
        alert('삭제에 실패했습니다.');
      }
    }
  };

  const getAbsolutePath = (path: string | null) => {
    return path ? `https://i11b108.p.ssafy.io/server/files/${path}` : '';
  };

  if (!show || !feedDetail) return null;

  return (
    <div className="feed-detail-modal modal-overlay">
      <div className={`modal-content ${editModalVisible ? 'blurred' : ''}`}>
        <button className="close-button" onClick={onClose}>
          &times;
        </button>
        <div className="feed-detail-left">
          <div className="feed-header">
            <div className="profile-image-container">
              {feedDetail.profileImagePath ? (
                <img
                  src={getAbsolutePath(feedDetail.profileImagePath)}
                  alt={feedDetail.nickname}
                  className="profile-image"
                />
              ) : (
                <UserProfileImageDefault className="profile-image-default" />
              )}
            </div>
            <div className="profile-info">
              <div className="nickname">{feedDetail.nickname}</div>
              <div className="feed-date">{new Date(feedDetail.createdAt).toLocaleString()}</div>
            </div>
            <div className="feed-icons">
              <div className="like-count">
                {feedDetail.likeCnt > 99 ? '99+' : feedDetail.likeCnt}
              </div>
              {feedDetail.isLike ? (
                <FullHeartIcon className="heart-button" onClick={handleLikeToggle} />
              ) : (
                <HeartIcon className="heart-button" onClick={handleLikeToggle} />
              )}
              <MenuIcon
                className={`dots-button ${currentUserId === feedDetail.userId ? 'visible' : ''}`}
                onClick={handleMenuToggle}
              />
              {menuVisible && <FeedDetailMenu onEdit={handleEdit} onDelete={handleDelete} />}
            </div>
          </div>
          <div className="feed-body">
            <div className="slider-container">
              {feedDetail.feedImagePathList.length > 0 && (
                <div className="image-slider">
                  <PrevButton className="prev-button" onClick={handlePrevImage} />
                  <img
                    src={getAbsolutePath(feedDetail.feedImagePathList[activeImageIndex])}
                    alt="Feed"
                    draggable="false"
                  />
                  <NextButton className="next-button" onClick={handleNextImage} />
                  <div className="image-counter">{`${activeImageIndex + 1} / ${feedDetail.feedImagePathList.length}`}</div>
                </div>
              )}
            </div>
            <div className="feed-content-container">
              <div className="feed-content">{feedDetail.content}</div>
              <div className="feed-tags">
                {feedDetail.tagList.map((tag, index) => (
                  <span key={index} className="tag">
                    #{tag}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
        <div className="feed-detail-right">
          <div className="comments" ref={commentsRef}>
            {comments.length === 0 ? (
              <div>댓글이 없습니다.</div>
            ) : (
              comments.map(comment => (
                <div key={comment.feedCommentId} className="comment">
                  <div className="comment-header">
                    <div className="comment-profile-image-container">
                      {comment.profileImagePath ? (
                        <img
                          src={getAbsolutePath(comment.profileImagePath)}
                          alt={comment.nickname}
                          className="comment-profile-image"
                        />
                      ) : (
                        <UserProfileImageDefault className="comment-profile-image-default" />
                      )}
                    </div>
                    <div className="comment-nickname">{comment.nickname}</div>
                    <div className="comment-date">
                      {new Date(comment.createdAt).toLocaleString()}
                    </div>
                  </div>
                  <div className="comment-content">{comment.content}</div>
                </div>
              ))
            )}
          </div>
          <div className="comment-input">
            <input
              type="text"
              placeholder="여기에 입력하세요."
              value={newComment}
              onChange={e => setNewComment(e.target.value)}
            />
            <button onClick={handleAddComment}>추가</button>
          </div>
        </div>
      </div>
      {editModalVisible && feedDetail && (
        <div className="feed-edit-modal">
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-header-title">
                <h2>글 수정</h2>
              </div>
              <span className="close" onClick={handleEditModalClose}>
                &times;
              </span>
            </div>
            <div className="modal-body">
              <EditFeed
                onClose={handleEditModalClose}
                feedId={feedId}
                initialContent={feedDetail.content}
                initialTags={feedDetail.tagList}
                initialImages={feedDetail.feedImagePathList}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default FeedDetailModal;
