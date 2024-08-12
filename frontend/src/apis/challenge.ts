import { Authapi } from './core';

export const postChallenge = async (formData: FormData) => {
  const response = await Authapi.postForm('/api/v1/challenge', formData);
  return response;
};
