import React, { useEffect, useRef, useState } from 'react';
import { ReactComponent as Close } from '../../assets/images/challenge/close.svg';
import { ReactComponent as AddImage } from '../../assets/images/img_add.svg';
import { postChallengeJoinAPI } from '../../apis/challenge';
import { isAxiosError } from 'axios';

interface ModalProps {
  onClose: () => void;
  challengeId: number;
}
export default function ChallengeDetailModal({ onClose,challengeId }: ModalProps) {
  const fileRef = useRef<HTMLInputElement>(null);
  const videoRef = useRef<HTMLVideoElement>(null);
  const [isLoading,setIsLoading] = useState<boolean>(false);
  const [videoPreview, setVideoPreview] = useState<string | null>(null);

  const CreateChallenge = async () => {
    if (!fileRef.current?.files) {
      alert('파일을 입력해주세요');
      return;
    }

    const formData = new FormData();
    formData.append('video', fileRef.current.files[0]);
    try{
        setIsLoading(true);
        const response = await postChallengeJoinAPI(challengeId,formData);
        console.log(response);
    }catch(err : unknown){
        if (isAxiosError(err)) {
            // Axios 에러인 경우
            alert(err.response?.data);
          }
    }finally{
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
    <div className="detailmodal">
      <div className="detailmodal-content">
        <div className="detailmodal-header">
          <div className="detailmodal-title">챌린지등록</div>
          <span className="close" onClick={onClose}>
            <Close />
          </span>
        </div>
        <div className="detailmodal-body">
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
              <button className="modal-btn" onClick={CreateChallenge}>
                등록
              </button>
            </div>
          </div>
        </div>
      </div>
      {isLoading ? <div className='loading'> 1분정도 소요됩니다:)</div> : null}
    </div>
  );
}
