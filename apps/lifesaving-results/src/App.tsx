import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { RouterProvider } from '@tanstack/react-router';
import { router } from './routes/_routes';

export const OAUTH_LOGIN_ENDPOINT = import.meta.env.VITE_OAUTH_LOGIN_ENDPOINT;
export const OAUTH_LOGOUT_ENDPOINT = import.meta.env.VITE_OAUTH_LOGOUT_ENDPOINT;
export const OAUTH_TOKEN_ENDPOINT = import.meta.env.VITE_OAUTH_TOKEN_ENDPOINT;
export const OAUTH_CLIENT_ID = import.meta.env.VITE_OAUTH_CLIENT_ID;
export const OAUTH_REDIRECT_URI = import.meta.env.VITE_OAUTH_REDIRECT_URI;
export const OAUTH_LOGOUT_REDIRECT_URI = import.meta.env.VITE_OAUTH_LOGOUT_REDIRECT_URI;

export const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const LOCALSTORAGE_TOKEN_KEY = 'access_token';
export const LOCALSTORAGE_REFRESH_TOKEN_KEY = 'refresh_token';

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
