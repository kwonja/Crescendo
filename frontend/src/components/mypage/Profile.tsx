import React, { ChangeEvent, useRef, useState } from 'react';
import { getUserId, IMAGE_BASE_URL } from '../../apis/core';
import { UserInfo } from '../../interface/user';
import { modifyProfileAPI } from '../../apis/user';

interface ProfileProps {
  userInfo: UserInfo;
  handleUpdate: (nickname: string, introduction: string) => void;
  handleFollow: () => void;
  userId: number;
}

export default function Profile({ userInfo, handleUpdate, handleFollow, userId }: ProfileProps) {
  const { nickname, introduction, isFollowing, profilePath } = userInfo;
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const nickeRef = useRef<HTMLInputElement>(null);
  const introRef = useRef<HTMLTextAreaElement>(null);
  const imageRef = useRef<HTMLImageElement>(null);
  const handleSaveClick = async () => {
    if (selectedFile) {
      const formData = new FormData();
      formData.append('profileImage', selectedFile);
      await modifyProfileAPI(formData);
    }
    const newNickname = nickeRef.current?.value || nickname;
    const newIntroduction = introRef.current?.value || introduction;
    handleUpdate(newNickname, newIntroduction);
    setIsEditing(prev => !prev);
  };
  const handleImageUpload = async (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedFile(file);
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        if (reader.result) {
          if (imageRef.current) {
            imageRef.current.src = reader.result as string; // Update image preview
          }
        }
      };
    }
  };
  return (
    <>
      <div className="profile">
        <div className="img">
          {isEditing ? (
            <>
              <img alt="유저 프로필" src={`${IMAGE_BASE_URL}${profilePath}`} ref={imageRef} />
              <label htmlFor="profile" className="editimg">
                업로드
              </label>
              <input id="profile" className="hidden" type="file" onChange={handleImageUpload} />
            </>
          ) : (
            <img src={`${IMAGE_BASE_URL}${profilePath}`} alt="유저 프로필" ref={imageRef} />
          )}
        </div>

        {isEditing ? (
          <>
            <div>닉네임</div>
            <input type="text" className="nickname_edit" defaultValue={nickname} ref={nickeRef} />
          </>
        ) : (
          <div className="nickname">{nickname}</div>
        )}

        {isEditing ? (
          <>
            <div>소개</div>
            <textarea className="content content_edit" defaultValue={introduction} ref={introRef} />
          </>
        ) : (
          <div className="content">{introduction}</div>
        )}
      </div>
      {userId === getUserId() ? (
        <>
          {isEditing ? (
            <div className="profile_save">
              <button className="w-1/4 bg-mainColor" onClick={handleSaveClick}>
                저장
              </button>
              <button className="w-1/4 bg-subColor" onClick={() => setIsEditing(prev => !prev)}>
                취소
              </button>
            </div>
          ) : (
            <div className="profile_edit">
              <button onClick={() => setIsEditing(prev => !prev)}>프로필 수정</button>
            </div>
          )}
        </>
      ) : (
        <>
          {isFollowing ? (
            <div className="profile_edit">
              <button onClick={handleFollow}>Unfollow</button>
            </div>
          ) : (
            <div className="profile_edit">
              <button onClick={handleFollow}>Follow</button>
            </div>
          )}
        </>
      )}
    </>
  );
}
