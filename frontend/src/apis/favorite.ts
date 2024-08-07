import { api } from './core';
import { favoriteRankListResponse, idolGroupInfo, idolInfo } from "../interface/favorite";
import { communityInfo } from '../interface/communityList';

export const getidolGroupListAPI = async () => {
  const response = await api.get('/api/v1/community', {params:{page:0, size:1000}});
  const communityList:communityInfo[] = response.data.content;
  //파싱
  const idolGroupList:idolGroupInfo[] = communityList.map((community)=>({groupId:community.idolGroupId, groupName:community.name}))
  return idolGroupList;
}

export const getIdolListAPI = async (idolGroupId:number) => {
  const response = await api.get(`/api/v1/idol/idol-group/${idolGroupId}/name`);
  const idolList:idolInfo[] = response.data.idolList;
  return idolList;
}


export const getFavoriteRankListAPI = async (page: number, size: number, idolId:number, sortByVotes:boolean) => {
    const params = {
      page,
      size,
      idolId,
      sortByVotes
    };
    const response = await api.get(`/api/v1/favorite-rank`, { params });
    console.log(response.data);
    return response.data as favoriteRankListResponse;
};    