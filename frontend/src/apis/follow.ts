import { api } from './core';

export const followerAPI = async (userId: number) => {
  const response = await api.get(`/api/v1/follow/user/${userId}/follower`);
  return response.data;
};

export const followingAPI = async (userId: number) => {
  const response = await api.get(`/api/v1/follow/user/${userId}/following`);
  return response.data;
};
