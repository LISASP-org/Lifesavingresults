import { Route } from '@tanstack/react-router';
import { Row, TwoColumns } from '../layout/Grid';
import { MainLayout } from '../layout/MainLayout';
import { rootRoute } from './_root';

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
