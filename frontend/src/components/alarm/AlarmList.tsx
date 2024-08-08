import React from 'react';
import { useAppSelector } from '../../store/hooks/hook';
import AlarmListItem from './AlarmListItem';

export default function AlarmList() {
  const { alramList } = useAppSelector(state => state.alarm);
  return (
    <>
      {alramList.map((item, index) => (
        <div key={item.alarmId} className="w-full alarmlist">
          <AlarmListItem alarm={item} />
        </div>
      ))}
    </>
  );
}
