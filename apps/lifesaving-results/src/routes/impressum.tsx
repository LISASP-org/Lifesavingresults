import { Route } from '@tanstack/react-router';
import { MainLayout } from '../layout/MainLayout';
import { Row } from '../layout/Row';
import { TwoColumns } from '../layout/TwoColumns';
import { rootRoute } from './_routes';

export const impressumRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/impressum',
  component: () => {
    return (
      <MainLayout title="Impressum">
        <Row>
          <TwoColumns>
            <div className="bg-dlrgRed-50">Left</div>
            <div className="bg-dlrgYellow-50">Right</div>
          </TwoColumns>
        </Row>
      </MainLayout>
    );
  },
});
