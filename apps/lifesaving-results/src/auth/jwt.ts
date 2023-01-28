import axios from 'axios';
import * as jose from 'jose';
import {
  LOCALSTORAGE_REFRESH_TOKEN_KEY,
  LOCALSTORAGE_TOKEN_KEY,
  OAUTH_CLIENT_ID,
  OAUTH_REDIRECT_URI,
  OAUTH_TOKEN_ENDPOINT,
} from '../App';

export type JWT = {
  exp: number; // 1674815944,
  iat: number; // 1674815644;
  auth_time: number; // 1674815447;
  jti: string; // "a3e88743-6ec2-47ad-a02e-fba6d7853611",
  iss: string; // "http://localhost:48080/realms/lifesaving",
  aud: string; // "account",
  sub: string; // "68e9ca04-12a3-4a0e-ab3a-bc8fd8f8ece6",
  typ: string; // "Bearer",
  azp: string; // "lifesaving-results-webapp",
  scope: string; // 'profile email';
  sid: string;
  email_verified: boolean;
  name: string;
  preferred_username: string;
  given_name: string;
  family_name: string;
};

export const getToken = (): { decoded: JWT; token: string } | undefined => {
  const token = localStorage.getItem(LOCALSTORAGE_TOKEN_KEY);
  if (!token) return undefined;

  const decodedToken = jose.decodeJwt(token);
  return { decoded: decodedToken as JWT, token };
};

export const fetchTokenWithCode = async (code: string) => {
  const res = await axios.post(
    OAUTH_TOKEN_ENDPOINT,
    new URLSearchParams({
      grant_type: 'authorization_code',
      client_id: OAUTH_CLIENT_ID,
      redirect_uri: OAUTH_REDIRECT_URI,
      code,
    }),
    {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    }
  );
  const responseData = await res.data;
  localStorage.setItem(LOCALSTORAGE_TOKEN_KEY, responseData['access_token']);
  localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN_KEY, responseData['refresh_token']);
};

const fetchNewToken = async () => {
  const refresh_token = localStorage.getItem(LOCALSTORAGE_REFRESH_TOKEN_KEY);
  if (!refresh_token) return undefined;

  try {
    const res = await axios.post(
      OAUTH_TOKEN_ENDPOINT,
      new URLSearchParams({
        grant_type: 'refresh_token',
        client_id: OAUTH_CLIENT_ID,
        redirect_uri: OAUTH_REDIRECT_URI,
        refresh_token,
      }),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );
    const responseData = await res.data;
    const token = responseData['access_token'];
    localStorage.setItem(LOCALSTORAGE_TOKEN_KEY, token);
    return token;
  } catch (e) {
    localStorage.removeItem(LOCALSTORAGE_TOKEN_KEY);
    localStorage.removeItem(LOCALSTORAGE_REFRESH_TOKEN_KEY);
    return undefined;
  }
};

export const getValidToken = async (): Promise<string> => {
  const now = Number((Date.now() + '').slice(0, 10));
  const token = getToken();
  const buffer = 10; // seconds

  const tokenValid = !!token && token.decoded.exp > now + buffer;

  if (tokenValid) return token?.token;
  else return await fetchNewToken();
};
