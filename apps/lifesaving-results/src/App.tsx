import { Button } from './components/Button';
import { MainLayout } from './layout/MainLayout';
import { Row } from './layout/Row';

const App = () => {
  return (
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
    </MainLayout>
  );
};

export const add = (i: number, j: number) => {
  return i + j;
};

export default App;
