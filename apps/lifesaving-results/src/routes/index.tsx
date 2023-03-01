import { Route } from '@tanstack/react-router';
import { MainLayout } from '../layout/MainLayout';
import { Row } from '../layout/Row';
import { rootRoute } from './_routes';

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
