import { OAUTH_CLIENT_ID, OAUTH_LOGIN_ENDPOINT, OAUTH_REDIRECT_URI } from '../App';

export const getLogin = () => {
  const queryParams = new URLSearchParams();
  queryParams.set('response_type', 'code');
  queryParams.set('client_id', OAUTH_CLIENT_ID);
  queryParams.set('redirect_uri', OAUTH_REDIRECT_URI);
  return { loginUrl: [OAUTH_LOGIN_ENDPOINT, queryParams.toString()].join('?') };
};
