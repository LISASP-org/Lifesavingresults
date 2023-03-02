import { Route } from '@tanstack/react-router';
import { Card } from '../components/Card';
import { useUser } from '../hooks/auth/useUser';
import { Row, TwoColumns } from '../layout/Grid';
import { MainLayout } from '../layout/MainLayout';
import { rootRoute } from './_root';

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
                <input id="given_name_input" defaultValue={user.given_name} />
                <label htmlFor="family_name_input">Nachname</label>
                <input id="family_name_input" defaultValue={user.family_name} />
              </form>
            </Card>
            <Card>Dein Profil</Card>
          </TwoColumns>
        </Row>
      </MainLayout>
    );
  },
});
