import {
  OAUTH_CLIENT_ID,
  OAUTH_LOGIN_ENDPOINT,
  OAUTH_LOGOUT_ENDPOINT,
  OAUTH_LOGOUT_REDIRECT_URI,
  OAUTH_REDIRECT_URI,
} from '../App';

export const getLoginUrl = () => {
  const queryParams = new URLSearchParams();
  queryParams.set('response_type', 'code');
  queryParams.set('client_id', OAUTH_CLIENT_ID);
  queryParams.set('redirect_uri', OAUTH_REDIRECT_URI);
  return [OAUTH_LOGIN_ENDPOINT, queryParams.toString()].join('?');
};

export const getLogoutUrl = () => {
  const queryParams = new URLSearchParams();
  queryParams.set('client_id', OAUTH_CLIENT_ID);
  queryParams.set('post_logout_redirect_uri', OAUTH_LOGOUT_REDIRECT_URI);
  return [OAUTH_LOGOUT_ENDPOINT, queryParams.toString()].join('?');
};
