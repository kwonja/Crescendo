import { api } from './core';
import { communityListResponse } from '../interface/communityList';
import { communityInfo } from '../interface/communityList';

export const getCommunityListAPI = async (page:number, size:number)  => {
  const params = {
    page,
    size
  }
  const response = await api.get(`/api/v1/community`, {params});
  return response.data as communityListResponse ;
};

export const getFavoriteListAPI = async () => {
  // 임시 데이터
  const tmpList: communityInfo[] = [
    {
      idolGroupId: 1,
      name: 'NewJeans',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 2,
      name: 'BTS',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 3,
      name: 'Aespa',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 4,
      name: 'Black Pink',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 5,
      name: '소녀시대',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
    {
      idolGroupId: 6,
      name: '오마이걸',
      profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
    },
  ];

  return tmpList;
}
