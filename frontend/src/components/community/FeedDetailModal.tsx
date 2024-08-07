import React, { useEffect, useState } from 'react';
import { Authapi } from '../../apis/core';
import { ReactComponent as HeartIcon } from '../assets/images/heart.svg';
import { ReactComponent as MenuIcon } from '../assets/images/menu.svg';
import { ReactComponent as CloseIcon } from '../assets/images/close.svg';
import '../scss/components/FeedDetailModal.scss';

type CommentType = {
  nickname: string;
  date: string;
  content: string;
};

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

const FeedDetailModal: React.FC<FeedDetailModalProps> = ({
  show,
  onClose,
  feedId,
}) => {
  const [feedDetail, setFeedDetail] = useState<FeedDetailResponse | null>(null);
  const [activeImageIndex, setActiveImageIndex] = useState(0);

  useEffect(() => {
    if (show) {
      Authapi.get(`/api/v1/community/feed/${feedId}`)
        .then((response) => {
          setFeedDetail(response.data);
        })
        .catch((error) => {
          console.error('Failed to fetch feed details', error);
        });
    }
  }, [show, feedId]);

  const handlePrevImage = () => {
    setActiveImageIndex((prevIndex) =>
      prevIndex > 0 ? prevIndex - 1 : feedDetail!.feedImagePathList.length - 1
    );
  };

  const handleNextImage = () => {
    setActiveImageIndex((prevIndex) =>
      prevIndex < feedDetail!.feedImagePathList.length - 1
        ? prevIndex + 1
        : 0
    );
  };

  if (!show || !feedDetail) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="close-button" onClick={onClose}>
          <CloseIcon />
        </button>
        <div className="feed-detail-left">
          <div className="feed-header">
            <img
              src={feedDetail.profileImagePath}
              alt={feedDetail.nickname}
              className="profile-image"
            />
            <div className="nickname">{feedDetail.nickname}</div>
            <div className="date">{new Date(feedDetail.createdAt).toLocaleString()}</div>
          </div>
          <div className="feed-body">
            {feedDetail.feedImagePathList.length > 0 && (
              <div className="image-slider">
                <button className="prev-button" onClick={handlePrevImage}>
                  &lt;
                </button>
                <img
                  src={feedDetail.feedImagePathList[activeImageIndex]}
                  alt="Feed"
                />
                <button className="next-button" onClick={handleNextImage}>
                  &gt;
                </button>
                <div className="image-counter">{`${activeImageIndex + 1} / ${feedDetail.feedImagePathList.length}`}</div>
              </div>
            )}
            <div className="feed-content">{feedDetail.content}</div>
          </div>
          <div className="feed-footer">
            <HeartIcon />
            <MenuIcon />
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
