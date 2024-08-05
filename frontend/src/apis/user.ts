import { api } from "./core";

export const UserSearchApi = async (page : number, size : number,nickname : string) => {
    const response = await api.get(`/api/v1/user/search?page=${page}&size=${size}&nickname=${nickname}`);
    return response.data;
  };