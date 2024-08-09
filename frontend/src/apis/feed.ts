import { Authapi } from './core';
import { FeedListResponse, getFeedListParams } from '../interface/feed';

export const getCommunityFeedListAPI = async (params:getFeedListParams) => {
    const response = await Authapi.get('/api/v1/community/feed', {params});
    return response.data as FeedListResponse;
}

export const toggleFeedLikeAPI = async (feedId:number) => {
    await Authapi.post(`/api/v1/community/feed/feed-like/${feedId}`);
}