import { Authapi } from './core';

export const chatroomlistAPI = async (page: number, size: number) => {
  const response = await Authapi.get(`/api/v1/dm/my-dm-group?page=${page}&size=${size}`);
  return response.data;
};

export const messagesAPI = async (
  UserId: number,
  page: number,
  size: number,
  dmGroupId: number,
) => {
  const response = await Authapi.get(
    `/api/v1/dm/dm-group/${dmGroupId}?loggedInUserId=${UserId}&page=1&size=${size}`,
  );
  return response.data;
};
