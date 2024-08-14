import React, { useEffect, useRef, useState } from 'react';
import { ReactComponent as Close } from '../../assets/images/challenge/close.svg';
import { ReactComponent as AddImage } from '../../assets/images/img_add.svg';
import { postChallengeAPI } from '../../apis/challenge';
import { isAxiosError } from 'axios';

interface ModalProps {
  onClose: () => void;
}
export default function ChallengeModal({ onClose }: ModalProps) {
  const titleRef = useRef<HTMLInputElement>(null);
  const timeRef = useRef<HTMLInputElement>(null);
  const fileRef = useRef<HTMLInputElement>(null);
  const videoRef = useRef<HTMLVideoElement>(null);
  const [videoPreview, setVideoPreview] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const CreateChallenge = async () => {
    if (!titleRef.current?.value || !timeRef.current?.value || !fileRef.current?.files) {
      alert('전부 입력해주세요');
      return;
    }

    const formData = new FormData();
    formData.append('title', titleRef.current.value);
    formData.append('endAt', timeRef.current.value);
    formData.append('video', fileRef.current.files[0]);
    try {
      setIsLoading(true);
      const response = await postChallengeAPI(formData);
      console.log(response);
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        // Axios 에러인 경우
        alert(err.response?.data);
      }
    } finally {
      setIsLoading(false);
      onClose();
    }
  };

  const handleFileChange = () => {
    if (fileRef.current?.files && fileRef.current.files.length > 0) {
      const file = fileRef.current.files[0];
      const videoURL = URL.createObjectURL(file);
      setVideoPreview(videoURL);
    }
  };

  useEffect(() => {
    return () => {
      if (videoPreview) {
        URL.revokeObjectURL(videoPreview);
      }
    };
  }, [videoPreview]);
  return (
    <div className="holdmodal">
      <div className="holdmodal-content">
        <div className="holdmodal-header">
          <div className="holdmodal-title">챌린지생성</div>
          <span className="close" onClick={onClose}>
            <Close />
          </span>
        </div>
        <div className="holdmodal-body">
          <div className="flex flex-row w-full">
            {videoPreview ? (
              <video
                ref={videoRef}
                controls
                src={videoPreview}
                className="video-preview object-cover"
              />
            ) : (
              <label htmlFor="video" className="video_label">
                <AddImage />
              </label>
            )}

            <input
              id="video"
              type="file"
              className="hidden"
              accept="video/mp4"
              ref={fileRef}
              onChange={handleFileChange}
            />
            <div className="right-content">
              <input
                type="text"
                className="input_title"
                placeholder="제목을 입력하세요"
                ref={titleRef}
              />
              <input className="input_date" id="cal" type="datetime-local" ref={timeRef} />
              {videoPreview && (
                <label htmlFor="video" className="video_label_add">
                  <AddImage className="w-16 h-16" />
                </label>
              )}

              <button className="modal-btn" onClick={CreateChallenge}>
                생성
              </button>
            </div>
          </div>
        </div>
      </div>
      {isLoading ? <div className="loading"> 1분정도 소요됩니다:)</div> : null}
    </div>
  );
}
