import React, { useState, useEffect, useCallback, useRef } from 'react';
import { api, Authapi } from '../../apis/core';
import { ReactComponent as HeartIcon } from '../../assets/images/Feed/white_heart.svg';
import { ReactComponent as MenuIcon } from '../../assets/images/Feed/white_dots.svg';
import { ReactComponent as NextButton } from '../../assets/images/Feed/next_button.svg';
import { ReactComponent as PrevButton } from '../../assets/images/Feed/prev_button.svg';
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
  commentId: number;
  userId: number;
  nickname: string;
  profileImagePath: string;
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
  const [newComment, setNewComment] = useState(''); // 새로운 댓글 입력을 위한 상태
  const [activeImageIndex, setActiveImageIndex] = useState(0);
  const [page, setPage] = useState(0); // 페이지 번호 관리
  const [loading, setLoading] = useState(false); // 로딩 상태 관리
  const [hasMoreComments, setHasMoreComments] = useState(true); // 더 이상 댓글이 없을 때 false로 설정
  const loader = useRef<HTMLDivElement | null>(null); // 로딩 요소에 대한 ref

  const loadComments = useCallback(
    (currentPage: number) => {
      if (!hasMoreComments) return; // 더 이상 댓글이 없을 경우 요청 중단

      setLoading(true);
      api
        .get(`/api/v1/community/feed/${feedId}/comment`, {
          params: { page: currentPage, size: 10 },
        })
        .then(response => {
          const newComments = response.data.content;
          if (newComments.length === 0) {
            setHasMoreComments(false); // 댓글이 더 이상 없음을 표시
          } else {
            setComments(prevComments => [...prevComments, ...newComments]);
          }
          setLoading(false);
        })
        .catch(error => {
          console.error('Error fetching comments:', error);
          setLoading(false);
        });
    },
    [feedId, hasMoreComments], // feedId 또는 hasMoreComments가 변경될 때만 loadComments 함수가 재생성됩니다.
  );

  useEffect(() => {
    if (show) {
      // 피드 상세 정보 가져오기
      api
        .get(`/api/v1/community/feed/${feedId}`)
        .then(response => setFeedDetail(response.data))
        .catch(error => console.error('Error fetching feed details:', error));

      // 초기 댓글 데이터 가져오기
      loadComments(0);
    }
  }, [show, feedId, loadComments]); // loadComments를 의존성 배열에 포함

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

    // eslint-disable-next-line no-console
    console.log(feedId);
    // eslint-disable-next-line no-console
    console.log(newComment);

    Authapi.post(`/api/v1/community/feed/${feedId}/comment`, {
      content: newComment,
    })
      .then(response => {
        const addedComment = response.data;

        setComments([...comments, addedComment]);
        setNewComment('');
      })
      .catch(error => {
        console.error('댓글 작성 오류:', error);
      });
  };

  const handleObserver = useCallback(
    (entries: IntersectionObserverEntry[]) => {
      const target = entries[0];
      if (target.isIntersecting && !loading && hasMoreComments) {
        setPage(prevPage => prevPage + 1); // 페이지 증가
      }
    },
    [loading, hasMoreComments], // loading 상태와 hasMoreComments에 따라 handleObserver 함수가 변경
  );

  useEffect(() => {
    if (page > 0) {
      loadComments(page); // 새로운 페이지의 댓글 로드
    }
  }, [page, loadComments]); // loadComments를 의존성 배열에 포함

  useEffect(() => {
    const option = {
      threshold: 0.1,
    };
    const observer = new IntersectionObserver(handleObserver, option);
    const currentLoader = loader.current; // ref 값을 로컬 변수에 저장
    if (currentLoader) observer.observe(currentLoader);
    return () => {
      if (currentLoader) observer.unobserve(currentLoader); // cleanup에서 로컬 변수를 사용
    };
  }, [handleObserver]);

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        onClose(); // ESC 키를 누르면 모달 닫기
      }
    };

    // 이벤트 리스너 추가
    window.addEventListener('keydown', handleKeyDown);

    // 컴포넌트가 언마운트되거나 모달이 닫힐 때 이벤트 리스너 제거
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [onClose]);

  const getAbsolutePath = (path: string) => {
    return `https://i11b108.p.ssafy.io/server/files/${path}`;
  };

  if (!show || !feedDetail) return null;

  return (
    <div className="feed-detail-modal modal-overlay">
      <div className="modal-content">
        <button className="close-button" onClick={onClose}>
          &times;
        </button>
        <div className="feed-detail-left">
          <div className="feed-header">
            <img
              src={getAbsolutePath(feedDetail.profileImagePath)}
              alt={feedDetail.nickname}
              className="profile-image"
            />
            <div className="profile-info">
              <div className="nickname">{feedDetail.nickname}</div>
              <div className="date">{new Date(feedDetail.createdAt).toLocaleString()}</div>
            </div>
            <div className="feed-icons">
              <HeartIcon className="heart-button" />
              <MenuIcon className="dots-button" />
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
          <div className="comments">
            {comments.length === 0 && !loading ? (
              <div>댓글이 없습니다.</div>
            ) : (
              comments.map(comment => (
                <div key={comment.commentId} className="comment">
                  <div className="comment-header">
                    <img
                      src={getAbsolutePath(comment.profileImagePath)}
                      alt={comment.nickname}
                      className="comment-profile-image"
                    />
                    <div className="comment-nickname">{comment.nickname}</div>
                    <div className="comment-date">
                      {new Date(comment.createdAt).toLocaleString()}
                    </div>
                  </div>
                  <div className="comment-content">{comment.content}</div>
                </div>
              ))
            )}
            {loading && <div ref={loader}>Loading...</div>}
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
    </div>
  );
};

export default FeedDetailModal;
