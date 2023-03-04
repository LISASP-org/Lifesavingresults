import { Route, useParams } from '@tanstack/react-router';
import { useGetCompetitionById } from '../../generated/resultsApi';
import { Row } from '../../layout/Grid';
import { MainLayout } from '../../layout/MainLayout';
import { rootRoute } from '../_root';

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
