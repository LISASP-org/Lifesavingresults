import { Route, useParams } from '@tanstack/react-router';
import { useGet } from '../../generated/resultsApi';
import { Row } from '../../layout/Row';
import { mainLayoutRoute } from '../_routes';

export const competitionByIdRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/competition/$competitionId',
  component: () => {
    const { competitionId } = useParams({ from: competitionByIdRoute.id });
    const { data: competition } = useGet(competitionId);
    return (
      <>
        <Row>
          {competition ? (
            <span>Hello Competition {competition.name}</span>
          ) : (
            <span>Unknown competition</span>
          )}
        </Row>
      </>
    );
  },
});
