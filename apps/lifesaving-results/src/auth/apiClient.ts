import type { AxiosRequestConfig } from 'axios';
import axios from 'axios';
import { BACKEND_URL } from '../App';
import { getValidToken } from './jwt';

export const apiClient = axios.create({ baseURL: BACKEND_URL });

// Add a request interceptor
apiClient.interceptors.request.use(
  async (config) => {
    config.headers.set('Authorization', `Bearer ${await getValidToken()}`);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor
apiClient.interceptors.response.use(
  (response) => {
    // Do something with response data
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const customInstance = <T>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig
): Promise<T> => {
  const source = axios.CancelToken.source();
  const promise = apiClient({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({ data }) => data);

  // @ts-ignore
  promise.cancel = () => {
    source.cancel('Query was cancelled');
  };

  return promise;
};
