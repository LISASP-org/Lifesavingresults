import { Route, useNavigate, useSearch } from '@tanstack/react-router';
import { useEffect, useState } from 'react';
import { z } from 'zod';
import { fetchTokenWithCode } from '../auth/jwt';
import { FeedbackLayout } from '../layout/FeedbackLayout';
import { rootRoute } from './_root';

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
          await fetchTokenWithCode(code);
          setTokensReceived(true);
        };
        func().catch((e) => {
          console.error(e);
          setError(true);
        });
      }
    }, [code]);

    useEffect(() => {
      if (tokensReceived) navigate({ to: '/', replace: true });
    }, [tokensReceived]);

    return (
      <FeedbackLayout>
        <div className="flex flex-row align-middle gap-2 pt-10 text-lg text-white font-bold">
          <span>Logging you in, please stand by ...</span>
        </div>
      </FeedbackLayout>
    );
  },
});
