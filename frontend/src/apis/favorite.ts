import { api } from './core';
import { favoriteRankListResponse } from "../interface/favorite";

export const getFavoriteRankListAPI = async (page: number, size: number) => {
    const params = {
      page,
      size,
    };
    const response = await api.get(`/api/v1/community`, { params });
    return response.data as favoriteRankListResponse;
};    