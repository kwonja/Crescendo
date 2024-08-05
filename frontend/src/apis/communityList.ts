import { api } from './core';
import { communityListResponse } from '../interface/communityList';
import { communityInfo } from '../interface/communityList';

export const getCommunityListAPI = async (page:number, size:number)  => {
  const params = {
    page,
    size
  }
  const response = await api.get(`/api/v1/community`, {params});
  // 임시 데이터 삽입
  response.data.content = tmpAllList.slice(page*size, page*size+size);
  response.data.last = response.data.content.length === 0;
  return response.data as communityListResponse ;
};

export const getFavoriteListAPI = async () => {
  // 임시 데이터
  const tmpFavoriteList: communityInfo[] = [
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

  return tmpFavoriteList;
}



const tmpAllList: communityInfo[] = [
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
  {
    idolGroupId: 7,
    name: '빅뱅',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 8,
    name: '르세라핌',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 9,
    name: '이달의소녀',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 10,
    name: '(여자)아이들',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 11,
    name: 'QWER',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 12,
    name: '마마무',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 13,
    name: 'NewJeans',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 14,
    name: 'BTS',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 15,
    name: 'Aespa',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 16,
    name: 'Black Pink',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 17,
    name: '소녀시대',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 18,
    name: '오마이걸',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 19,
    name: '빅뱅',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 20,
    name: '르세라핌',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 21,
    name: '이달의소녀',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 22,
    name: '(여자)아이들',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 23,
    name: 'QWER',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
  {
    idolGroupId: 24,
    name: '마마무',
    profile: 'https://i.ibb.co/t3rdL7G/313885-438531-4716.jpg',
  },
];