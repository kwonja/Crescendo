import axios from "axios";
export const BASE_URL = "http://i11b108.p.ssafy.io:8000";

const config = {
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
};
export const api = axios.create(config);
export const Authapi = axios.create(config);

//Authapi에 .interceptors 같은거 붙여서 인증예외처리도 가능!

// Authapi.interceptors.request.use((config) => {
//   config.headers.Authorization = `Bearer ${getLocalStorageToken()}`;
//   return config;
// });
