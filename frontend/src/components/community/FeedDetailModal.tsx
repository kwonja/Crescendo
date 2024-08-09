import React, { useState, useEffect } from 'react';
import { api } from '../../apis/core';
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

type FeedDetailModalProps = {
  show: boolean;
  onClose: () => void;
  feedId: number;
};

const FeedDetailModal: React.FC<FeedDetailModalProps> = ({ show, onClose, feedId }) => {
  const [feedDetail, setFeedDetail] = useState<FeedDetailResponse | null>(null);
  const [activeImageIndex, setActiveImageIndex] = useState(0);

  useEffect(() => {
    // eslint-disable-next-line no-console
    console.log('피드 상세 요청 보냅니다!!');
    if (show) {
      api
        // .get(`/api/v1/community/feed/${feedId}`)
        .get(`/api/v1/community/feed/36`)
        .then(response => {
          // eslint-disable-next-line no-console
          console.log('API response:', response.data);
          setFeedDetail(response.data);
        })
        .catch(error => console.error('Error fetching feed details:', error));
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
            {/* 댓글 리스트 표시 (실제 데이터로 대체 필요) */}
            <div className="comment">
              <div className="comment-header">
                <div className="comment-nickname">Nickname</div>
                <div className="comment-date">24.07.22, 16:43</div>
              </div>
              <div className="comment-content">댓글입니다.</div>
            </div>
          </div>
          <div className="comment-input">
            <input type="text" placeholder="여기에 입력하세요." />
          </div>
        </div>
      </div>
    </div>
  );
};

export default FeedDetailModal;
