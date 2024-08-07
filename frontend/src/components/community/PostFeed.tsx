import React, { useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Authapi } from '../../apis/core';
import { ReactComponent as AddImage } from '../../assets/images/img_add.svg';
import { ReactComponent as AddTag } from '../../assets/images/tag_add.svg';
import { ReactComponent as RemoveTag } from '../../assets/images/tag_remove.svg';
import { ReactComponent as RemoveIcon } from '../../assets/images/remove_icon.svg';
import '../../scss/components/community/_postfeed.scss';

type ImageWithId = {
  id: number;
  file: File;
};

const FeedForm = () => {
  const [images, setImages] = useState<ImageWithId[]>([]);
  const [content, setContent] = useState('');
  const [tags, setTags] = useState<string[]>([]);
  const [newTag, setNewTag] = useState('');
  const fileInputRef = useRef<HTMLInputElement>(null);
  const { idolGroupId } = useParams<{ idolGroupId: string }>();

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const fileList = Array.from(e.target.files);
      const newImages = fileList.map(file => ({
        id: Date.now() + Math.random(),
        file: file,
      }));

      // 중복 검사
      const existingFiles = new Set(images.map(image => image.file.name));
      const filteredImages = newImages.filter(image => !existingFiles.has(image.file.name));

      if (images.length + filteredImages.length <= 10) {
        setImages(prevImages => [...prevImages, ...filteredImages]);
      } else {
        alert('이미지는 최대 10장까지 업로드 가능합니다.');
      }

      // 동일 파일 재업로드를 위해 초기화
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  const handleImageRemove = (id: number) => {
    setImages(images.filter(image => image.id !== id));
  };

  const handleTagAdd = () => {
    if (newTag && !tags.includes(newTag) && tags.length < 10) {
      setTags([...tags, newTag]);
      setNewTag('');
    } else if (tags.length >= 10) {
      alert('태그는 최대 10개까지 추가 가능합니다.');
    }
  };

  const handleTagRemove = (tag: string) => {
    setTags(tags.filter(t => t !== tag));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('content', content);
    images.forEach(image => formData.append('imageList', image.file));
    tags.forEach(tag => formData.append('tagList', tag));
    formData.append('idolGroupId', idolGroupId ?? '');

    // 데이터 확인용 로그 출력
    // for (let pair of formData.entries()) {
    //   console.log(pair[0] + ': ' + pair[1]);
    // }

    try {
      const response = await Authapi.post('/api/v1/community/feed', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      if (response.status === 201) {
        alert('피드가 성공적으로 작성되었습니다.');
      }
    } catch (error) {
      alert('피드 작성에 실패했습니다.');
      console.error(error);
    }
  };

  const handleAddImageClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    if (e.target.value.length <= 400) {
      setContent(e.target.value);
    }
  };

  const handleNewTagKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleTagAdd();
    }
  };

  return (
    <form onSubmit={handleSubmit} className="feed-form">
      <div className="form-group image-upload-group">
        <div className="image-preview">
          {images.map((imageWithId, index) => (
            <div key={imageWithId.id} className="image-wrapper">
              <img src={URL.createObjectURL(imageWithId.file)} alt={`preview-${index}`} />
              <RemoveIcon
                className="remove-image"
                onClick={() => handleImageRemove(imageWithId.id)}
              >
                취소
              </RemoveIcon>
            </div>
          ))}
        </div>
        {images.length < 10 && (
          <div className="add-image-container" onClick={handleAddImageClick}>
            <AddImage className="custom-file-upload" />
          </div>
        )}
        <input
          type="file"
          multiple
          accept="image/*"
          onChange={handleImageChange}
          ref={fileInputRef}
          style={{ display: 'none' }}
        />
        <span className="image-count">{images.length}/10</span>
      </div>

      <div className="form-group">
        <div className="textarea-wrapper">
          <textarea
            placeholder="내용을 입력하세요"
            rows={5}
            value={content}
            onChange={handleContentChange}
          />
          <span className="char-count">{content.length}/400</span>
        </div>
      </div>

      <div className="form-group">
        <div className="tags-wrapper">
          <div className="tags">
            {tags.map((tag, index) => (
              <span key={index} className="tag">
                #{tag}{' '}
                <RemoveTag className="removetag-button" onClick={() => handleTagRemove(tag)} />
              </span>
            ))}
            {tags.length < 10 && (
              <div className="addtag-container">
                <input
                  type="text"
                  value={newTag}
                  onChange={e => setNewTag(e.target.value)}
                  onKeyDown={handleNewTagKeyDown}
                  placeholder="#태그"
                  maxLength={10}
                  style={{ minWidth: '50px', width: 'auto' }}
                />
                <AddTag className="addtag-button" onClick={handleTagAdd} />
              </div>
            )}
          </div>
        </div>
      </div>
      <div className="submit-container">
        <button type="submit" className="submit-button">
          작성
        </button>
      </div>
    </form>
  );
};

export default FeedForm;
