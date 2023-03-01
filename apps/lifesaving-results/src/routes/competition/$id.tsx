import { Route, useParams } from '@tanstack/react-router';
import { useGetCompetitionById } from '../../generated/resultsApi';
import { MainLayout } from '../../layout/MainLayout';
import { Row } from '../../layout/Row';
import { rootRoute } from '../_routes';

export const competitionByIdRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/competition/$competitionId',
  component: () => {
    const { competitionId } = useParams({ from: competitionByIdRoute.id });
    const { data: competition } = useGetCompetitionById(competitionId);
    return (
      <MainLayout title="Competition">
        <Row>
          {competition ? (
            <span>Hello Competition {competition.name}</span>
          ) : (
            <span>Unknown competition</span>
          )}
        </Row>
      </MainLayout>
    );
  },
});
