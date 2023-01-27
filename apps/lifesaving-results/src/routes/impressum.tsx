import { Route } from '@tanstack/react-router';
import { useEffect } from 'react';
import { apiClient } from '../auth/apiClient';
import { Row } from '../layout/Row';
import { TwoColumns } from '../layout/TwoColumns';
import { mainLayoutRoute } from './_routes';

export const impressumRoute = new Route({
  getParentRoute: () => mainLayoutRoute,
  path: '/impressum',
  component: () => {
    useEffect(() => {
      const func = async () => {
        const res = await apiClient.get('https://reqres.in/api/users/2');
        console.log(res.data);
      };
      func().then(() => {});
    }, []);

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
