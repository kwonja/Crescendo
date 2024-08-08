import React, { useState } from 'react';
import { ReactComponent as HeartIcon } from '../../assets/images/Feed/heart.svg';
import { ReactComponent as MenuIcon } from '../../assets/images/Feed/dots.svg';
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
  // 임시 데이터로 기본 틀 확인
  const feedDetail: FeedDetailResponse = {
    userId: 1,
    profileImagePath: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    nickname: 'Nickname',
    createdAt: '2024-08-08T12:34:56Z',
    likeCnt: 99,
    feedImagePathList: [
      'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
      'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
      'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
      'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
      'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    ],
    content:
      '모든 것이 주마등처럼 스쳐 지나간다. 만들면서도 2년이 이렇게 빠르게 지나갔다는게 진짜 긴가민가하네',
    commentCnt: 21,
    tagList: ['#뉴진스', '#2주년'],
  };

  const [activeImageIndex, setActiveImageIndex] = useState(0);

  const handlePrevImage = () => {
    setActiveImageIndex(prevIndex =>
      prevIndex > 0 ? prevIndex - 1 : feedDetail.feedImagePathList.length - 1,
    );
  };

  const handleNextImage = () => {
    setActiveImageIndex(prevIndex =>
      prevIndex < feedDetail.feedImagePathList.length - 1 ? prevIndex + 1 : 0,
    );
  };

  if (!show) return null;

  return (
    <div className="feed-detail-modal modal-overlay">
      <div className="modal-content">
        <button className="close-button" onClick={onClose}>
          &times;
        </button>
        <div className="feed-detail-left">
          <div className="feed-header">
            <img
              src={feedDetail.profileImagePath}
              alt={feedDetail.nickname}
              className="profile-image"
            />
            <div className="profile-info">
              <div className="nickname">{feedDetail.nickname}</div>
              <div className="date">{new Date(feedDetail.createdAt).toLocaleString()}</div>
            </div>
          </div>
          <div className="feed-body">
            {feedDetail.feedImagePathList.length > 0 && (
              <div className="image-slider">
                <button className="prev-button" onClick={handlePrevImage}>
                  &lt;
                </button>
                <img src={feedDetail.feedImagePathList[activeImageIndex]} alt="Feed" />
                <button className="next-button" onClick={handleNextImage}>
                  &gt;
                </button>
                <div className="image-counter">{`${activeImageIndex + 1} / ${feedDetail.feedImagePathList.length}`}</div>
              </div>
            )}
            <div className="feed-content">{feedDetail.content}</div>
            <div className="feed-tags">
              {feedDetail.tagList.map((tag, index) => (
                <span key={index} className="tag">
                  {tag}
                </span>
              ))}
            </div>
          </div>
          <div className="feed-icons">
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
