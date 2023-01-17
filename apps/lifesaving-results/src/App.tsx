import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { Button } from './components/Button';
import { User } from './components/User';
import { MainLayout } from './layout/MainLayout';
import { Row } from './layout/Row';

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <MainLayout>
        <Row>
          <div className="text-dlrgRed">Primary</div>
        </Row>
        <Row>
          <div className="text-dlrgYellow">Secondary</div>
        </Row>
        <Row css="flex gap-2 items-baseline">
          <div className="text-dlrgGray">Tertiary</div>
        </Row>
        <Row>
          <Button text="Submit" icon="th" />
        </Row>
        <Row>
          <User />
        </Row>
      </MainLayout>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export const add = (i: number, j: number) => {
  return i + j;
};

export default App;
