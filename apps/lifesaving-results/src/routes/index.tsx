import { Route } from '@tanstack/react-router';
import { Row } from '../layout/Grid';
import { MainLayout } from '../layout/MainLayout';
import { rootRoute } from './_root';

export const indexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/',
  component: () => {
    return (
      <MainLayout title="">
        <Row>Hello Index</Row>
      </MainLayout>
    );
  },
});
