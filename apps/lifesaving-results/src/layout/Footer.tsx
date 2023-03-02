import classNames from 'classnames';
import { Link } from '../components/Link';
import { Row } from './Grid';

type Props = {
  className?: string;
};

export const Footer = (props: Props) => {
  return (
    <div className={classNames(['bg-dlrgRed text-white text-xs', props.className])}>
      <Row>
        <div className="flex flex-row justify-between">
          <span>© DLRG, 2023</span>
          <Link to="/impressum">Impressum</Link>
        </div>
      </Row>
    </div>
  );
};
