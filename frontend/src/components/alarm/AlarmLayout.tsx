import React, { useEffect } from 'react';
import AlarmList from './AlarmList';
import { getAlarmList } from '../../features/alarm/alarmSlice';
import { useAppDispatch } from '../../store/hooks/hook';

export default function AlarmLayout() {
  const dispatch = useAppDispatch();
  useEffect( ()=>{
    dispatch(getAlarmList());
  },[dispatch])
  return (
  <div className="alarmlayout">
    <div className="title">알림목록</div>
    <AlarmList/>
  </div>);
}
