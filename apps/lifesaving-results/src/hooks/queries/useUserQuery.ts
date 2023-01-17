import { useQuery } from '@tanstack/react-query';
import type { AxiosResponse } from 'axios';
import axios from 'axios';

type User = {
  data: {
    id: number;
    email: string;
    first_name: string;
    last_name: string;
    avatar: string;
  };
  support: {
    url: string;
    text: string;
  };
};

export const useUserQuery = ({ userId }: { userId: string }) =>
  useQuery(
    ['user', userId],
    async () => {
      await new Promise((res) => setTimeout(() => res('promise'), 3000));
      const response = await axios.get<any, AxiosResponse<User>>(
        `https://reqres.in/api/users/${userId}`
      );
      return response.data;
    },
    { staleTime: Infinity }
  );
