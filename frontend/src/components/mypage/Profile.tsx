import React, { ChangeEvent, useState } from 'react';
import { DEFAULT_IMAGE, getUserId } from '../../apis/core';
import { UserInfo } from '../../interface/user';
import { modifyIntroductionAPI, modifyNicknameAPI, modifyProfileAPI } from '../../apis/user';

interface ProfileProps {
  userInfo: UserInfo;
  setProfileImage: (value: string) => void;
  setNickname: (value: string) => void;
  setIntroduction: (value: string) => void;
  userId: number;
}
export default function Profile({
  userInfo,
  setProfileImage,
  setNickname,
  setIntroduction,
  userId,
}: ProfileProps) {
  const { profilePath, nickname, introduction, isFollowing } = userInfo;

  const [newisFollowing, setNewisFolowing] = useState<boolean>(isFollowing);
  const [newprofileImage, setNewprofileImage] = useState(profilePath);
  const [newnickname, setNewnickname] = useState(nickname);
  const [newintroduction, setNewintroduction] = useState(introduction);

  const [isEditing, setIsEditing] = useState(false);

  const onErrorImg = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
    (e.target as HTMLImageElement).src = DEFAULT_IMAGE;
  };

  const handleSaveClick = () => {
    setIsEditing(false);
    Promise.all([
      modifyIntroductionAPI(userId),
      modifyNicknameAPI(userId),
      modifyProfileAPI(userId),
    ]);
  };
  const handleCancelClick = () => {
    setIsEditing(false);
    setNewintroduction(introduction);
    setNewnickname(nickname);
    setNewprofileImage(profilePath);
  };

  const handleImageUpload = (e: ChangeEvent<HTMLInputElement>): void => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = event => {
        setNewprofileImage(event.target?.result as string);
      };
    }
  };

  return (
    <>
      <div className="profile">
        <div className="img">
          <img src={`${newprofileImage}`} alt="유저 프로필" onError={onErrorImg} />
          {isEditing ? (
            <>
              <label htmlFor="profile" className="editimg">
                업로드
              </label>
              <input
                id="profile"
                className="hidden"
                type="file"
                accept="image/*"
                onChange={handleImageUpload}
              />
            </>
          ) : null}
        </div>

        {isEditing ? (
          <>
            <div>닉네임</div>
            <input
              type="text"
              className="nickname_edit"
              value={newnickname}
              onChange={e => setNewnickname(e.target.value)}
            />
          </>
        ) : (
          <div className="nickname">{nickname}</div>
        )}

        {isEditing ? (
          <>
            <div>소개</div>
            <textarea
              className="content content_edit"
              value={newintroduction === null ? '' : introduction}
              onChange={e => setNewintroduction(e.target.value)}
            />
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
              <button className="w-1/4 bg-subColor" onClick={handleCancelClick}>
                취소
              </button>
            </div>
          ) : (
            <div className="profile_edit">
              <button onClick={() => setIsEditing(true)}>프로필 수정</button>
            </div>
          )}
        </>
      ) : (
        <>
          {newisFollowing ? (
            <div className="profile_edit">
              <button onClick={() => setNewisFolowing(false)}>Unfollow</button>
            </div>
          ) : (
            <div className="profile_edit">
              <button onClick={() => setNewisFolowing(true)}>follow</button>
            </div>
          )}
        </>
      )}
    </>
  );
}
