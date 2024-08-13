import { Authapi } from './core';

export const postChallengeAPI = async (formData: FormData) => {
  const response = await Authapi.postForm('/api/v1/challenge', formData);
  return response;
};

export const getChallengeAPI = async(page: number,size : number,title : string, sortBy : string) =>{
  const response = await Authapi.get(`/api/v1/challenge?page=${page}&size=${size}`)
  return response.data;
}
