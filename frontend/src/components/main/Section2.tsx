import React from 'react';
const Section2 = () => {
  return (
    <div className="section2-content">
      <div className="section2-title_box " data-aos="fade-right" data-aos-duration="1500">
        <div className="section2-title">당신의 최애를 자랑하세요!</div>
        <div className="section2-subtitle">
          자신이 좋아하는 아이돌이 어느정도 인기인지 확인할 수 있어요
        </div>
      </div>
      <div className="w-2/5 section2-phone" data-aos="fade-up" data-aos-duration="1500"></div>
    </div>
  );
};
export default React.memo(Section2);
