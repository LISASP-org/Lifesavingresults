import { Route } from '@tanstack/react-router';
import { MainLayout } from '../../layout/MainLayout';
import { Row } from '../../layout/Row';
import { rootRoute } from '../_routes';

export const competitionIndexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/competition',
  component: () => {
    return (
      <MainLayout title="Competitions">
        <Row>Hello Competitions</Row>
      </MainLayout>
    );
  },
});
