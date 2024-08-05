// src/components/community/PostFeed.tsx
import React, { useState } from 'react';
import '../../scss/components/community/_postfeed.scss';

const FeedForm = () => {
  const [images, setImages] = useState<File[]>([]);
  const [content, setContent] = useState('');
  const [tags, setTags] = useState<string[]>([]);
  const [newTag, setNewTag] = useState('');

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const fileList = Array.from(e.target.files);
      if (images.length + fileList.length <= 10) {
        setImages([...images, ...fileList]);
      } else {
        alert('이미지는 최대 10장까지 업로드 가능합니다.');
      }
    }
  };

  const handleImageRemove = (imageName: string) => {
    setImages(images.filter(image => image.name !== imageName));
  };

  const handleTagAdd = () => {
    if (newTag && !tags.includes(newTag)) {
      setTags([...tags, newTag]);
      setNewTag('');
    }
  };

  const handleTagRemove = (tag: string) => {
    setTags(tags.filter(t => t !== tag));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // API 호출 로직 추가
  };

  return (
    <form onSubmit={handleSubmit} className="feed-form">
      <div className="form-group">
        <input type="file" multiple accept="image/*" onChange={handleImageChange} />
        <div className="image-preview">
          {images.map((image, index) => (
            <div key={index} className="image-wrapper">
              <img src={URL.createObjectURL(image)} alt={`preview-${index}`} />
              <button
                type="button"
                className="remove-image"
                onClick={() => handleImageRemove(image.name)}
              >
                취소
              </button>
            </div>
          ))}
        </div>
        <span className="image-count">{images.length}/10</span>
      </div>

      <div className="form-group">
        <textarea
          placeholder="내용을 입력하세요"
          rows={5}
          value={content}
          onChange={e => setContent(e.target.value)}
        />
        <span className="char-count">{content.length}/400</span>
      </div>

      <div className="form-group">
        <label>태그</label>
        <div className="tags-wrapper">
          <div className="tags">
            {tags.map((tag, index) => (
              <span key={index} className="tag">
                #{tag} <span onClick={() => handleTagRemove(tag)}>x</span>
              </span>
            ))}
            <input
              type="text"
              value={newTag}
              onChange={e => setNewTag(e.target.value)}
              placeholder="#태그 추가"
              maxLength={10}
            />
            <button type="button" onClick={handleTagAdd}>
              +
            </button>
          </div>
          <button type="submit" className="submit-button">
            작성
          </button>
        </div>
      </div>
    </form>
  );
};

export default FeedForm;
