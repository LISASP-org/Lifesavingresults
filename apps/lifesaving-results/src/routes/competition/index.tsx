import { Route } from '@tanstack/react-router';
import { Row } from '../../layout/Grid';
import { MainLayout } from '../../layout/MainLayout';
import { rootRoute } from '../_root';

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
