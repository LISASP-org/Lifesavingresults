import { Outlet, ReactRouter, RootRoute, Route } from '@tanstack/react-router';
import { indexRoute } from '.';
import { MainLayout } from '../layout/MainLayout';
import { callbackRoute } from './callback';
import { competitionByIdRoute } from './competition/$id';
import { impressumRoute } from './impressum';

export const mainLayoutRoute = new Route({
  getParentRoute: () => rootRoute,
  id: 'mainLayout',
  component: () => (
    <MainLayout>
      <Outlet />
    </MainLayout>
  ),
});

export const rootRoute = new RootRoute({
  component: () => <Outlet />,
});

const routeTree = rootRoute.addChildren([
  callbackRoute,
  mainLayoutRoute.addChildren([indexRoute, impressumRoute, competitionByIdRoute]),
]);

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router;
  }
}

export const router = new ReactRouter({
  routeTree,
  // defaultPreload: 'intent',
});
