import type { JWT } from '../../auth/jwt';
import { getToken } from '../../auth/jwt';

export const useUser = () => {
  const token = getToken();
  const decodedToken = token?.decoded;
  const name = decodedToken ? (decodedToken as JWT)['name'] : undefined;
  const given_name = decodedToken ? (decodedToken as JWT)['given_name'] : undefined;
  const family_name = decodedToken ? (decodedToken as JWT)['family_name'] : undefined;
  const preferred_username = decodedToken
    ? (decodedToken as JWT)['preferred_username']
    : undefined;

  return {
    loggedIn: !!token,
    name,
    given_name,
    family_name,
    preferred_username,
  };
};
