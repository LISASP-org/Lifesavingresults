import { Route, useParams } from '@tanstack/react-router';
import { Row } from '../../layout/Row';
import { mainLayoutRoute } from '../_routes';

export const competitionByIdRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/competition/$competitionId',
  parseParams: (raw) => ({ competitionId: Number(raw.competitionId) }),
  stringifyParams: (params) => ({ competitionId: params.competitionId + '' }),
  component: () => {
    const { competitionId } = useParams({ from: competitionByIdRoute.id });
    return (
      <>
        <Row>
          {competitionId ? (
            <span>Hello Competition {competitionId}</span>
          ) : (
            <span>Unknown competition</span>
          )}
        </Row>
      </>
    );
  },
});
