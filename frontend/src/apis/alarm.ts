import { Authapi } from './core';

export const getAlarmAPI = async (page: number, size: number) => {
  const response = await Authapi.get(`/api/v1/alarm?page=${page}&size=${size}`);
  return response.data;
};
