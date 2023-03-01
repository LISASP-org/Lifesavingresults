import { Route } from '@tanstack/react-router';
import { Card } from '../components/Card';
import { useUser } from '../hooks/auth/useUser';
import { MainLayout } from '../layout/MainLayout';
import { Row } from '../layout/Row';
import { TwoColumns } from '../layout/TwoColumns';
import { rootRoute } from './_routes';

export const profileRoute = new Route({
  getParentRoute: () => rootRoute,
  path: '/profile',
  component: () => {
    const user = useUser();
    return (
      <MainLayout title="Profil">
        <Row>
          <Card>Dein Profil</Card>
        </Row>
        <Row>
          <TwoColumns>
            <Card title="Profil">
              <form className="grid grid-cols-form gap-2">
                <label htmlFor="given_name_input">Vorname</label>
                <input id="given_name_input" value={user.given_name} />
                <label htmlFor="family_name_input">Nachname</label>
                <input id="family_name_input" value={user.family_name} />
              </form>
            </Card>
            <Card>Dein Profil</Card>
          </TwoColumns>
        </Row>
      </MainLayout>
    );
  },
});
