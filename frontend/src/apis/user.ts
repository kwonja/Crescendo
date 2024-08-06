import { Authapi, api } from './core';

export const UserSearchApi = async (page: number, size: number, nickname: string) => {
  const response = await api.get(
    `/api/v1/user/search?page=${page}&size=${size}&nickname=${nickname}`,
  );
  return response.data;
};

export const getMyFeedAPI = async (page: number, size: number) => {
  const response = await Authapi.get(`/api/v1/community/feed/my-feed?page=${page}&size=${size}`);
  return response.data;
};
