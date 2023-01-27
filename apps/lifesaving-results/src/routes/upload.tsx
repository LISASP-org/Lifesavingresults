import { Route } from '@tanstack/react-router';
import { Row } from '../layout/Row';
import { mainLayoutRoute } from './_routes';

export const uploadRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/upload',
  component: () => {
    return (
      <>
        <Row>Hello Upload</Row>
      </>
    );
  },
});
