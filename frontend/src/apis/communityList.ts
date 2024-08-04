import { api } from './core';
import { communityListResponse } from '../interface/communityList';

export const getCommunityListAPI = async (page:number, size:number)  => {
  const params = {
    page,
    size
  }
  const response = await api.get(`/api/v1/community`, {params});
  return response.data as communityListResponse ;
};
