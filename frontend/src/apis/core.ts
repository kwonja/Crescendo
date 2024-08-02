import axios from 'axios';
export const BASE_URL = 'http://i11b108.p.ssafy.io:8000';

const config = {
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
};
export const api = axios.create(config);
export const Authapi = axios.create(config);

//Authapi에 .interceptors 같은거 붙여서 인증예외처리도 가능!

Authapi.interceptors.request.use(config => {
  config.headers.Authorization = `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nlc3MtdG9rZW4iLCJpYXQiOjE3MjI2MDMzNjQsImV4cCI6MTcyMjYwNjk2NCwidXNlcklkIjoxfQ.fJPPFadrLpfpJM97XW7S7s2EPT-Q1mfYnPrabq0atco`;
  return config;
});
