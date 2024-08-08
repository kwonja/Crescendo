import { Authapi } from './core';
import { FeedListResponse, getCommunityFeedListParams } from '../interface/feed';




export const getCommunityFeedListAPI = async (params:getCommunityFeedListParams) => {
    const response = await Authapi.get('/api/v1/community/feed', {params});
    return response.data as FeedListResponse;
}