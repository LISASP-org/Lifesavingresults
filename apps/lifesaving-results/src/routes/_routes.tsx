import { Outlet, ReactRouter, RootRoute, Route } from '@tanstack/react-router';
import { indexRoute } from '.';
import { MainLayout } from '../layout/MainLayout';
import { competitionByIdRoute } from './competition/$id';
import { impressumRoute } from './impressum';
import { listRoute } from './list';
import { uploadRoute } from './upload';

export const mainLayoutRoute = new Route({
  getParentRoute: () => rootRoute,
  id: 'mainLayout',
  component: () => (
    <MainLayout>
      <Outlet />
    </MainLayout>
  ),
});

const rootRoute = new RootRoute({
  component: () => <Outlet />,
});

const routeTree = rootRoute.addChildren([
  mainLayoutRoute.addChildren([
    indexRoute,
    uploadRoute,
    listRoute,
    impressumRoute,
    competitionByIdRoute,
  ]),
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
