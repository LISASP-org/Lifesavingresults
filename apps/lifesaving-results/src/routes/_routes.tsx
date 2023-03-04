import { ReactRouter } from '@tanstack/react-router';
import { indexRoute } from '.';
import { callbackRoute } from './callback';
import { competitionIndexRoute } from './competition';
import { competitionByIdRoute } from './competition/$id';
import { impressumRoute } from './impressum';
import { logout_callbackRoute } from './logout_callback';
import { profileRoute } from './profile';
import { rootRoute } from './_root';

const routeTree = rootRoute.addChildren([
  callbackRoute,
  logout_callbackRoute,
  indexRoute,
  competitionIndexRoute,
  impressumRoute,
  competitionByIdRoute,
  profileRoute,
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
