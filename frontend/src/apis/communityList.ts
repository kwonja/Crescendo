import { api, Authapi } from './core';
import { communityListResponse } from '../interface/communityList';
import { communityInfo } from '../interface/communityList';

export const getCommunityListAPI = async (page: number, size: number) => {
  const params = {
    page,
    size,
  };
  const response = await api.get(`/api/v1/community`, { params });
  return response.data as communityListResponse;
};

export const getFavoriteListAPI = async () => {
  const params = {
    page: 0,
    size: 1000,
  };

  const response = await Authapi.get('/api/v1/community/favorites', { params });

  return response.data.content as communityInfo[];
};
