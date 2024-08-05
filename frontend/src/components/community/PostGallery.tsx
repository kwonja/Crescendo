import React, { useState } from 'react';
import '../../scss/components/community/_postfeed.scss';

const GalleryForm = () => {
  const [images, setImages] = useState<File[]>([]);
  const [content, setContent] = useState('');
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('팬아트');

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

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // API 호출 로직 추가
  };

  return (
    <form onSubmit={handleSubmit} className="gallery-form">
      <div className="form-group">
        <label>이미지 첨부</label>
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
          <span>{images.length}/10</span>
        </div>
      </div>

      <div className="form-group">
        <label>카테고리</label>
        <select value={category} onChange={e => setCategory(e.target.value)}>
          <option value="팬아트">팬아트</option>
          <option value="굿즈">굿즈</option>
        </select>
      </div>

      <div className="form-group">
        <label>제목</label>
        <input type="text" value={title} onChange={e => setTitle(e.target.value)} />
      </div>

      <div className="form-group">
        <label>내용</label>
        <textarea rows={5} value={content} onChange={e => setContent(e.target.value)} />
        <span className="char-count">{content.length}/400</span>
      </div>

      <button type="submit" className="submit-button">
        작성
      </button>
    </form>
  );
};

export default GalleryForm;
