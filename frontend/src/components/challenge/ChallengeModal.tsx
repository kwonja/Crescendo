import React  from 'react'
import { ReactComponent as Close } from '../../assets/images/challenge/close.svg';
import { ReactComponent as AddImage } from '../../assets/images/img_add.svg';
// import { ReactComponent as Calendar } from '../../assets/images/challenge/calendar.svg';

interface ModalProps{
    onClose: () => void;
}
export default function ChallengeModal({onClose} : ModalProps) {



    const CreateChallenge = async() =>{

    }
    return (
    <div className="holdmodal">
          <div className="holdmodal-content">
            <div className="holdmodal-header">
              <div className='holdmodal-title'>챌린지생성</div>
              <span className="close" onClick={onClose}>
                <Close/>
              </span>
            </div>
            <div className="holdmodal-body">
                <div className='flex flex-row w-full'>
                <label htmlFor='video' className='video_label'>
                        <AddImage/>
                </label>
                <input id="video"type='file' className='hidden'/>
                <div className='right-content'>
                    <input type="text" className='input_title' placeholder='제목을 입력하세요'/>
                    <input
                    className='input_date'
                                id="cal"
                                type="date"
                        />


                        <button className="modal-btn"onClick={CreateChallenge}>생성</button>
                           
                </div>
                </div>
            </div>
          </div>
    </div>
  )
}
