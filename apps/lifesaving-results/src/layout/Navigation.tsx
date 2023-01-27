import { Link } from '../components/Link';
import { Row } from './Row';

type Props = {};

export const Navigation = (props: Props) => {
  return (
    <div className="bg-dlrgRed shadow">
      <Row>
        <div className="flex flex-row justify-between items-center">
          <Link to="/">
            <img width={120} height={19} src="/DLRGWortmarke.svg" />
          </Link>
          <ul className="flex flex-row gap-2 text-white">
            <li>
              <Link to="/upload">Upload</Link>
            </li>
            <li>
              <Link to="/list" search={{ num: 4 }}>
                List
              </Link>
            </li>
            <li>
              <Link to="/competition/$competitionId" params={{ competitionId: 2 }}>
                Competition
              </Link>
            </li>
          </ul>
        </div>
      </Row>
    </div>
  );
};
