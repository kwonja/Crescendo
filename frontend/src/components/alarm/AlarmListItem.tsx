import React from 'react';
import { timeAgo } from '../../utils/TimeAgo';
import { Alarm } from '../../interface/alarm';
import { Channel } from './ChannelHook';
import { deleteAlamrAPI, readAlarm } from '../../apis/alarm';
import { useAppDispatch } from '../../store/hooks/hook';
import { decrementUnRead, deleteAlarm } from '../../features/alarm/alarmSlice';

interface AlarmItemProps {
  alarm: Alarm;
}
export default function AlarmListItem({ alarm }: AlarmItemProps) {
  const dispatch = useAppDispatch();
  const { alarmChannelId, content,createdAt, alarmId } = alarm;
  const handleReadAlarm = async (alarmId: number) => {
    try {
      await readAlarm(alarmId);
      dispatch(decrementUnRead());
    } catch (err) {
      // console.log(err);
    }
  };

  const handleDeleteAlarm = async (alarmId: number) => {
    dispatch(deleteAlarm(alarmId));
    try {
      await deleteAlamrAPI(alarmId);
    } catch (err: unknown) {
      // console.log(err)
    }
  };

  return (
    <div className="alarmlistitem" onClick={() => handleReadAlarm(alarmId)}>
      <div className="cont w-8/12">
        <div className="flex flex-row gap-3 w-full">
          <div className="nickname">{content.substring(0,content.indexOf('님'))}</div>
          <div>{Channel(alarmChannelId)}</div>
        </div>
        <div className="content w-11/12">{content}</div>
      </div>
      <div className="cursor-pointer text-sm absolute right-3 bottom-2" onClick={() => handleDeleteAlarm(alarmId)}>
        삭제
      </div>
      <div className="lastchattime">{timeAgo(createdAt)}</div>
    </div>
  );
}
