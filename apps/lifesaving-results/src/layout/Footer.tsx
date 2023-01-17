import classNames from 'classnames';
import { Link } from '../components/Link';
import { Row } from './Row';

type Props = {
  css?: string;
};

export const Footer = (props: Props) => {
  return (
    <div className={classNames(['bg-dlrgRed text-white text-xs', props.css])}>
      <Row>
        <div className="flex flex-row justify-between">
          <span>© DLRG, 2023</span>
          <Link href="#">Impressum</Link>
        </div>
      </Row>
    </div>
  );
};
