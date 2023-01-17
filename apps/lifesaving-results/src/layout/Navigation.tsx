import { Link } from '../components/Link';
import { Row } from './Row';

type Props = {};

export const Navigation = (props: Props) => {
  return (
    <div className="bg-dlrgRed shadow">
      <Row>
        <div className="flex flex-row justify-between items-center">
          <img width={120} src="/DLRGWortmarke.svg" />
          <ul className="flex flex-row gap-2 text-white">
            <li>
              <Link href="#">Upload</Link>
            </li>
            <li>
              <Link href="#">List</Link>
            </li>
          </ul>
        </div>
      </Row>
    </div>
  );
};
