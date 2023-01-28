import { Spinner } from '@blueprintjs/core';
import { Route, useNavigate } from '@tanstack/react-router';
import { useEffect } from 'react';
import { FeedbackLayout } from '../layout/FeedbackLayout';
import { mainLayoutRoute } from './_routes';

export const logout_callbackRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/logout_callback',
  component: () => {
    const navigate = useNavigate();
    useEffect(() => {
      localStorage.clear();
      navigate({ to: '/', replace: true });
    }, []);

    return (
      <FeedbackLayout>
        <div className="flex flex-row align-middle gap-2 pt-10 text-white text-lg font-bold">
          <Spinner size={20} />
          <span>Logging you out, please stand by ...</span>
        </div>
      </FeedbackLayout>
    );
  },
});
