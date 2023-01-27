import { Route, useMatchRoute, useParams, useSearch } from '@tanstack/react-router';
import z from 'zod';
import { Row } from '../layout/Row';
import { ThreeColumns } from '../layout/ThreeColumns';
import { mainLayoutRoute } from './_routes';

export const listRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/list',
  validateSearch: z.object({
    num: z.number(),
  }),
  component: () => {
    const s = useSearch();
    const p = useParams();

    const m = useMatchRoute();

    return (
      <>
        <Row>Hello List</Row>
        <Row>
          <ThreeColumns>
            <div className="bg-dlrgRed-50">Left</div>
            <div className="bg-dlrgYellow-50">Middle</div>
            <div className="bg-dlrgGray-50">Right</div>
          </ThreeColumns>
        </Row>
      </>
    );
  },
});
