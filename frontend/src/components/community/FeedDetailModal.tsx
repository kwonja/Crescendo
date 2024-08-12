import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, Authapi, getUserId } from '../../apis/core';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFeedLike } from '../../features/feed/communityFeedSlice';
import { ReactComponent as HeartIcon } from '../../assets/images/Feed/white_heart.svg';
import { ReactComponent as FullHeartIcon } from '../../assets/images/Feed/white_fullheart.svg';
import { ReactComponent as FeedMenuIcon } from '../../assets/images/Feed/white_dots.svg';
import { ReactComponent as CommentMenuIcon } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as NextButton } from '../../assets/images/Feed/next_button.svg';
import { ReactComponent as PrevButton } from '../../assets/images/Feed/prev_button.svg';
import { ReactComponent as UserProfileImageDefault } from '../../assets/images/UserProfile/reduser.svg';
import { ReactComponent as ReplyIcon } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as CommentWriteButton } from '../../assets/images/white_write.svg';
import FeedDetailMenu from './DropdownMenu';
import CommentMenu from './DropdownMenu';  // 이 부분은 그대로 유지합니다.
import EditFeed from './EditFeed';
import '../../scss/components/community/_feeddetailmodal.scss';

const MAX_COMMENT_LENGTH = 50;

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
  replyCnt: number;
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
  const [commentMenuVisible, setCommentsMenuVisible] = useState<{ [key: number]: boolean }>({});
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editingCommentId, setEditingCommentId] = useState<number | null>(null); // 수정 중인 댓글 ID 상태 추가
  const [editedContent, setEditedContent] = useState<string>(''); // 수정 중인 댓글 내용 상태 추가
  const commentsRef = useRef<HTMLDivElement | null>(null);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const currentUserId = getUserId();

  const loadFeedDetail = async () => {
    try {
      const response = await Authapi.get(`/api/v1/community/feed/${feedId}`);
      setFeedDetail(response.data);
    } catch (error) {
      console.error('Error fetching feed details:', error);
    }
  };

  const loadComments = async () => {
    try {
      const response = await api.get(`/api/v1/community/feed/${feedId}/comment`, {
        params: { page: 0, size: 5 },
      });
      setComments(response.data.content);
    } catch (error) {
      console.error('Error fetching comments:', error);
    }
  };

  const handleNewCommentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length <= MAX_COMMENT_LENGTH) {
      setNewComment(e.target.value);
    }
  };

  useEffect(() => {
    if (show) {
      setComments([]);
      loadFeedDetail();
      loadComments();
    }
  }, [show, feedId]);

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

  const handleCommentMenuToggle = (commentId: number) => {
    setCommentsMenuVisible(prevState => ({
      ...prevState,
      [commentId]: !prevState[commentId], // 해당 댓글의 메뉴 가시성을 토글
    }));
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

  // 댓글 수정 모드로 전환
  const handleCommentEditClick = (commentId: number, currentContent: string) => {
    setEditingCommentId(commentId);
    setEditedContent(currentContent);
  };

  // 댓글 수정 완료
  const handleCommentEditSubmit = (commentId: number) => {
    if (editedContent.trim() === '') {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    Authapi.put(`/api/v1/community/feed/${feedId}/comment/${commentId}`, { content: editedContent })
      .then(() => {
        setEditingCommentId(null); // 수정 모드 종료
        loadComments(); // 수정 후 댓글 목록 다시 불러오기
      })
      .catch(error => {
        console.error('댓글 수정 오류:', error);
        alert('댓글 수정에 실패했습니다.');
      });
  };

  // 댓글 삭제
  const handleCommentDelete = (commentId: number) => {
    const confirmDelete = window.confirm('댓글을 삭제하시겠습니까?');
    if (confirmDelete) {
      Authapi.delete(`/api/v1/community/feed/${feedId}/comment/${commentId}`)
        .then(() => {
          loadComments();
        })
        .catch(error => {
          console.error('댓글 삭제 오류:', error);
        });
    }
  };

  const getAbsolutePath = (path: string | null) => {
    return path ? `https://www.crescendo.o-r.kr/server/files/${path}` : '';
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
              <FeedMenuIcon
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
                    <div className="comment-user-info">
                      <div className="comment-nickname">{comment.nickname}</div>
                      <div className="comment-date">
                        {new Date(comment.createdAt).toLocaleString()}
                      </div>
                    </div>
                    <CommentMenuIcon
                      className={`comment-dots-button ${currentUserId === comment.userId ? 'visible' : ''}`}
                      onClick={() => handleCommentMenuToggle(comment.feedCommentId)}
                    />
                    <div className="comment-menu">
                      {commentMenuVisible[comment.feedCommentId] && (
                        <CommentMenu
                          onEdit={() => handleCommentEditClick(comment.feedCommentId, comment.content)}
                          onDelete={() => handleCommentDelete(comment.feedCommentId)}
                        />
                      )}
                    </div>
                  </div>
                  <div className="comment-content">
                    {editingCommentId === comment.feedCommentId ? (
                      <div className="editing-comment">
                        <input
                          type="text"
                          className='comment-edit-input'
                          value={editedContent}
                          onChange={(e) => setEditedContent(e.target.value)}
                          onKeyDown={(e) => {
                            if (e.key === 'Enter') handleCommentEditSubmit(comment.feedCommentId);
                          }}
                        />
                        <button onClick={() => handleCommentEditSubmit(comment.feedCommentId)}>
                          수정
                        </button>
                      </div>
                    ) : (
                      <p>{comment.content}</p>
                    )}
                  </div>
                  <div className="reply-icon-container">
                    <ReplyIcon className="reply-icon" />
                    <div className="reply-count">
                      {comment.replyCnt}개의 <div>&nbsp;답글</div>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
          <div className="comment-input-container">
            <input
              type="text"
              placeholder="여기에 입력하세요."
              value={newComment}
              onChange={handleNewCommentChange}
            />
            <CommentWriteButton className="comment-write-button" onClick={handleAddComment} />
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
