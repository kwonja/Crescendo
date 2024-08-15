import React from 'react';
import { ReactComponent as Image1 } from '../../assets/images/main/section2-1.svg';
import { ReactComponent as Image2 } from '../../assets/images/main/section2-2.svg';
import { ReactComponent as Image3 } from '../../assets/images/main/section2-3.svg';
const Section2 = () => {
  return (
    <div className="section2-content">
      <div className="section2-title_box " data-aos="fade-right" data-aos-duration="1500">
        <div className="section2-title">당신의 최애를 자랑하세요!</div>
        <div className="section2-subtitle">
          자신이 좋아하는 아이돌이 어느정도 인기인지 확인할 수 있어요
        </div>
      </div>
      <div className="section2-1" data-aos="fade-down" data-aos-duration="2000"><Image1 className='w-4/5'/></div>
      <div className="section2-2" data-aos="fade-down" data-aos-duration="1500"><Image2 className='w-4/5'/></div>
      <div className="section2-3" data-aos="fade-down" data-aos-duration="2500"><Image3 className='w-4/5'/></div>
    </div>
  );
};
export default React.memo(Section2);
