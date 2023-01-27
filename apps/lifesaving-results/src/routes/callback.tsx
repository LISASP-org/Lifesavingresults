import { Route, useNavigate, useSearch } from '@tanstack/react-router';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { z } from 'zod';
import {
  LOCALSTORAGE_REFRESH_TOKEN_KEY,
  LOCALSTORAGE_TOKEN_KEY,
  OAUTH_CLIENT_ID,
  OAUTH_REDIRECT_URI,
  OAUTH_TOKEN_ENDPOINT,
} from '../App';
import { rootRoute } from './_routes';

export const callbackRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/callback',
  validateSearch: z.object({
    code: z.string(),
  }),
  component: () => {
    const { code } = useSearch({ from: callbackRoute.id });
    const navigate = useNavigate();

    const [tokensReceived, setTokensReceived] = useState(false);
    const [error, setError] = useState(false);

    useEffect(() => {
      if (code) {
        const func = async () => {
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
          localStorage.setItem(
            LOCALSTORAGE_REFRESH_TOKEN_KEY,
            responseData['refresh_token']
          );
          setTokensReceived(true);
        };
        func().catch((e) => {
          console.error(e);
          setError(false);
        });
      }
    }, [code]);

    useEffect(() => {
      if (tokensReceived) navigate({ to: '/', replace: true });
    }, [tokensReceived]);

    return <span>Logging you in, please stand by ...</span>;
  },
});
