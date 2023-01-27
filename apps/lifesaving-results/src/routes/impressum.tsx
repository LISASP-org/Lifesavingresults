import { Route } from '@tanstack/react-router';
import { Row } from '../layout/Row';
import { TwoColumns } from '../layout/TwoColumns';
import { mainLayoutRoute } from './_routes';

export const impressumRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/impressum',
  component: () => {
    return (
      <>
        <Row>
          <TwoColumns>
            <div className="bg-dlrgRed-50">Left</div>
            <div className="bg-dlrgYellow-50">Right</div>
          </TwoColumns>
        </Row>
      </>
    );
  },
});
