import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Authapi, getUserId } from '../../apis/core';
import { useAppDispatch } from '../../store/hooks/hook';
import { toggleFeedLike } from '../../features/communityDetail/communityDetailSlice';
import { ReactComponent as FeedHeartIcon } from '../../assets/images/Feed/white_heart.svg';
import { ReactComponent as FeedFullHeartIcon } from '../../assets/images/Feed/white_fullheart.svg';
import { ReactComponent as CommentHeartIcon } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as CommentFullHeartIcon } from '../../assets/images/Feed/fullheart.svg';
import { ReactComponent as FeedMenuIcon } from '../../assets/images/Feed/white_dots.svg';
import { ReactComponent as CommentMenuIcon } from '../../assets/images/Feed/dots.svg';
import { ReactComponent as NextButton } from '../../assets/images/Feed/next_button.svg';
import { ReactComponent as PrevButton } from '../../assets/images/Feed/prev_button.svg';
import { ReactComponent as UserProfileImageDefault } from '../../assets/images/UserProfile/reduser.svg';
import { ReactComponent as ReplyIcon } from '../../assets/images/Feed/comment.svg';
import { ReactComponent as CommentWriteButton } from '../../assets/images/white_write.svg';
import FeedDetailMenu from './DropdownMenu';
import CommentMenu from './DropdownMenu';
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
  likeCnt: number;
  isLike?: boolean;
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
  const [activeMenuId, setActiveMenuId] = useState<number | null>(null);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editingCommentId, setEditingCommentId] = useState<number | null>(null); // 수정 중인 댓글 ID 상태 추가
  const [editedContent, setEditedContent] = useState<string>(''); // 수정 중인 댓글 내용 상태 추가
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
  }, [feedId]);

  const loadComments = useCallback(async () => {
    try {
      const response = await Authapi.get(`/api/v1/community/feed/${feedId}/comment`, {
        params: { page: 0, size: 100 },
      });
      //eslint-disable-next-line no-console
      console.log('Comments:', response);
      setComments(response.data.content);
    } catch (error) {
      console.error('Error fetching comments:', error);
    }
  }, [feedId]);

  const handleNewCommentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length <= MAX_COMMENT_LENGTH) {
      setNewComment(e.target.value);
    }
  };

  const handleNewCommentKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleAddComment();
    }
  };

  useEffect(() => {
    if (show) {
      setComments([]);
      loadFeedDetail();
      loadComments();
    }
  }, [show, feedId, loadComments, loadFeedDetail]);

  useEffect(() => {
    if (!show) {
      setEditingCommentId(null);
      setEditedContent('');
      setActiveMenuId(null);
    }
  }, [show]);

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

  const handleFeedLikeToggle = () => {
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

  const handleCommentLikeToggle = (commentId: number) => {
    // 현재 댓글의 상태를 찾아서 저장
    const commentIndex = comments.findIndex(comment => comment.feedCommentId === commentId);
    if (commentIndex === -1) return; // 댓글이 없을 경우 아무것도 하지 않음

    const originalComment = comments[commentIndex];
    const updatedComment = {
      ...originalComment,
      isLike: !originalComment.isLike,
      likeCnt: originalComment.isLike ? originalComment.likeCnt - 1 : originalComment.likeCnt + 1,
    };

    // 로컬 상태를 먼저 업데이트
    const updatedComments = [...comments];
    updatedComments[commentIndex] = updatedComment;
    setComments(updatedComments);

    // 서버에 요청 보내기
    Authapi.post(`/api/v1/community/feed/feed-comment-like/${commentId}`).catch(error => {
      console.error('댓글 좋아요 오류:', error);

      // 실패할 경우 원래 상태로 롤백
      setComments(prevComments => {
        const rollbackComments = [...prevComments];
        rollbackComments[commentIndex] = originalComment;
        return rollbackComments;
      });
    });
  };

  const handleMenuToggle = (feedId: number) => {
    if (activeMenuId === feedId) {
      setActiveMenuId(null); // 이미 열려 있는 메뉴를 닫음
    } else {
      setActiveMenuId(feedId); // 새로운 메뉴를 열음
    }
  };

  const handleCommentMenuToggle = (commentId: number) => {
    if (activeMenuId === commentId) {
      setActiveMenuId(null); // 이미 열려 있는 메뉴를 닫음
    } else {
      setActiveMenuId(commentId); // 새로운 메뉴를 열음
    }
  };

  const handleEdit = () => {
    setActiveMenuId(null);
    setEditModalVisible(true);
  };

  const handleEditModalClose = async () => {
    await loadFeedDetail();
    setEditModalVisible(false);
  };

  // 피드 삭제
  const handleDelete = async () => {
    setActiveMenuId(null);
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
    handleCommentMenuToggle(commentId);
    setEditingCommentId(commentId);
    setEditedContent(currentContent);
  };

  // 댓글 수정 모드 취소
  const handleCommentEditCancel = () => {
    setEditingCommentId(null);
    setEditedContent('');
  };

  // 댓글 수정 완료
  const handleCommentEditSubmit = (commentId: number) => {
    if (editedContent.trim() === '') {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    const formData = new FormData();
    formData.append('content', editedContent);

    //eslint-disable-next-line no-console
    console.log(formData);

    Authapi.patch(`/api/v1/community/feed/${feedId}/comment/${commentId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
      .then(() => {
        setEditingCommentId(null); // 수정 모드 종료
        loadComments(); // 수정 후 댓글 목록 다시 불러오기
      })
      .catch(error => {
        if (error.response) {
          const { status, data } = error.response;

          // 400 Bad Request 처리
          if (status === 400 && data.exception === 'InvalidFeedCommentContentFormatException') {
            alert('댓글 내용 형식이 올바르지 않습니다.');
          }
          // 404 Not Found 처리
          else if (status === 404 && data.exception === 'FeedCommentNotFoundException') {
            alert('댓글을 찾을 수 없습니다.');
          }
          // 기타 상태 코드 처리
          else {
            console.error('댓글 수정 오류:', status, data);
            alert(`댓글 수정에 실패했습니다. 서버 응답: ${status}`);
          }
        } else if (error.request) {
          // 요청이 전송되었으나 응답이 없는 경우
          console.error('응답 없음:', error.request);
          alert('서버로부터 응답을 받지 못했습니다.');
        } else {
          // 요청을 보내기 전에 발생한 오류
          console.error('요청 설정 오류:', error.message);
          alert('요청을 처리하는 중에 오류가 발생했습니다.');
        }
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
              <div className="feed-like-count">
                {feedDetail.likeCnt > 99 ? '99+' : feedDetail.likeCnt}
              </div>
              {feedDetail.isLike ? (
                <FeedFullHeartIcon className="feed-heart-button" onClick={handleFeedLikeToggle} />
              ) : (
                <FeedHeartIcon className="feed-heart-button" onClick={handleFeedLikeToggle} />
              )}
              <FeedMenuIcon
                className={`feed-dots-button ${currentUserId === feedDetail.userId ? 'visible' : ''}`}
                onClick={() => handleMenuToggle(feedId)}
              />
              <div className="feed-menu">
                {activeMenuId === feedId && (
                  <FeedDetailMenu onEdit={handleEdit} onDelete={handleDelete} />
                )}
              </div>
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
                    <div className="comment-icons">
                      <div className="comment-like-count">
                        {comment.likeCnt > 99 ? '99+' : comment.likeCnt}
                      </div>
                      {comment.isLike ? (
                        <CommentFullHeartIcon
                          className="comment-heart-button"
                          onClick={() => handleCommentLikeToggle(comment.feedCommentId)}
                        />
                      ) : (
                        <CommentHeartIcon
                          className="comment-heart-button"
                          onClick={() => handleCommentLikeToggle(comment.feedCommentId)}
                        />
                      )}
                      <CommentMenuIcon
                        className={`comment-dots-button ${currentUserId === comment.userId ? 'visible' : ''}`}
                        onClick={() => handleCommentMenuToggle(comment.feedCommentId)}
                      />
                      <div className="comment-menu">
                        {activeMenuId === comment.feedCommentId && (
                          <CommentMenu
                            onEdit={() =>
                              handleCommentEditClick(comment.feedCommentId, comment.content)
                            }
                            onDelete={() => handleCommentDelete(comment.feedCommentId)}
                          />
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="comment-content">
                    {editingCommentId === comment.feedCommentId ? (
                      <div className="editing-comment">
                        <input
                          type="text"
                          className="comment-edit-input"
                          value={editedContent}
                          onChange={e => setEditedContent(e.target.value)}
                          onKeyDown={e => {
                            if (e.key === 'Enter') handleCommentEditSubmit(comment.feedCommentId);
                          }}
                        />
                        <button
                          className="comment-edit-submit-button"
                          onClick={() => handleCommentEditSubmit(comment.feedCommentId)}
                        >
                          수정
                        </button>
                        <button
                          className="comment-edit-exit-button"
                          onClick={handleCommentEditCancel}
                        >
                          취소
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
              onKeyDown={handleNewCommentKeyDown}
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
