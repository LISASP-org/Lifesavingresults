import { Route, useParams } from '@tanstack/react-router';
import { MainLayout } from '../../layout/MainLayout';
import { Row } from '../../layout/Row';
import { rootRoute } from '../_routes';

export const competitionByIdRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/competition/$competitionId',
  parseParams: (raw) => ({ competitionId: Number(raw.competitionId) }),
  stringifyParams: (params) => ({ competitionId: params.competitionId + '' }),
  component: () => {
    const { competitionId } = useParams({ from: competitionByIdRoute.id });
    return (
      <MainLayout title="Competition">
        <Row>
          {competitionId ? (
            <span>Hello Competition {competitionId}</span>
          ) : (
            <span>Unknown competition</span>
          )}
        </Row>
      </MainLayout>
    );
  },
});
