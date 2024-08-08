import React, { useEffect, useState } from 'react'
import { getUserId, IMAGE_BASE_URL } from '../../apis/core';
import { ReactComponent as User } from '../../assets/images/Chat/user.svg';
import { timeAgo } from '../../utils/TimeAgo';
import { Alarm } from '../../interface/alarm';
import { getUserInfoAPI } from '../../apis/user';
import { UserInfo } from '../../interface/user';
import { Channel } from './ChannelHook';
import { readAlarm } from '../../apis/alarm';
import { useAppDispatch } from '../../store/hooks/hook';
import { decrementUnRead } from '../../features/alarm/alarmSlice';

interface AlarmItemProps {
    alarm: Alarm;
}
export default function AlarmListItem({ alarm }: AlarmItemProps) {
  const dispatch = useAppDispatch();
  const { alarmChannelId,content,relatedId,createdAt,alarmId} = alarm;
  const [info,setInfo] = useState<UserInfo | null>(null);

  const handleReadAlarm = async(alarmId : number)=>{

    try{
      await readAlarm(alarmId);
      dispatch(decrementUnRead());
    }catch(err){
      // console.log(err);
    }
  }
  useEffect( ()=>{
    const getUserInfo = async()=>{
      const response = await getUserInfoAPI(relatedId,getUserId());
      setInfo(response.data);
    }
    getUserInfo();
  },[relatedId])
    
  return (
    <div className="alarmlistitem" onClick={()=> handleReadAlarm(alarmId)}>
      {info?.profilePath ? (
        <div className="m-1.5 h-12 w-12">
          <img
            src={`${IMAGE_BASE_URL}${info.profilePath}`}
            alt="프로필"
            className="w-full h-full rounded-full"
          />
        </div>
      ) : (
        <div className="m-1.5 w-12 h-12">
          <User className='w-full h-full'/>
        </div>
      )}
      <div className="cont w-8/12">
        <div className='flex flex-row gap-3 w-full'>
        <div className="nickname">{info?.nickname}</div><div>{Channel(alarmChannelId)}</div>
        </div>
        <div className="content w-full">{content}ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd</div>
      </div>

      <div className="lastchattime">{timeAgo(createdAt)}</div>
    </div>
  );
}
