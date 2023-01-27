import { Icon } from '@blueprintjs/core';
import { getLogin } from '../auth/login';
import { Link } from '../components/Link';
import { useUser } from '../hooks/auth/useUser';
import { Row } from './Row';

type Props = {};

export const Navigation = (props: Props) => {
  const { loggedIn, name, preferred_username } = useUser();
  const { loginUrl } = getLogin();

  return (
    <div className="bg-dlrgRed shadow">
      <Row>
        <div className="flex flex-row justify-between items-center">
          <Link to="/">
            <img width={120} height={19} src="/DLRGWortmarke.svg" />
          </Link>
          <ul className="flex flex-row gap-2 text-white">
            <li>
              <Link to="/competition/$competitionId" params={{ competitionId: 2 }}>
                Competition
              </Link>
            </li>
            <li>
              {loggedIn ? (
                <span>Hi, {name ?? preferred_username}</span>
              ) : (
                <a href={loginUrl} className="underline outline-none hover:text-inherit">
                  Login <Icon icon="user" />
                </a>
              )}
            </li>
          </ul>
        </div>
      </Row>
    </div>
  );
};
