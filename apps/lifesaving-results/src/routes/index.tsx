import { Route } from '@tanstack/react-router';
import { Row } from '../layout/Row';
import { mainLayoutRoute } from './_routes';

export const indexRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/',
  component: () => {
    return (
      <>
        <Row>Hello Index</Row>
      </>
    );
  },
});
