import { Authapi } from './core';
import { GalleryListResponse, getGalleryListParams } from '../interface/gallery';

export const getCommunityFanArtListAPI = async (params: getGalleryListParams) => {
  const response = await Authapi.get('/api/v1/community/fan-art', { params });
  return response.data as GalleryListResponse;
};

export const toggleFanArtLikeAPI = async (fanArtId: number) => {
  await Authapi.post(`/api/v1/community/fan-art/fan-art-like/${fanArtId}`);
};
