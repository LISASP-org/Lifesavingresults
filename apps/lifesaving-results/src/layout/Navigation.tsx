import { Icon } from '@blueprintjs/core';
import { getLoginUrl, getLogoutUrl } from '../auth/login';
import { Link } from '../components/Link';
import { useUser } from '../hooks/auth/useUser';
import { Row } from './Row';

type Props = {};

export const Navigation = (props: Props) => {
  const { loggedIn, name, preferred_username } = useUser();

  return (
    <div className="bg-dlrgRed shadow">
      <Row>
        <div className="flex flex-row justify-between items-center">
          <Link to="/">
            <img width={120} height={19} src="/DLRGWortmarke.svg" />
          </Link>
          <ul className="flex flex-row gap-2 text-white">
            <li>
              {loggedIn ? (
                <div className="flex flex-row gap-2">
                  <span>Hi, {name ?? preferred_username}</span>
                  <a
                    href={getLogoutUrl()}
                    className="underline outline-none hover:text-inherit"
                  >
                    Logout <Icon icon="log-out" />
                  </a>
                </div>
              ) : (
                <a
                  href={getLoginUrl()}
                  className="underline outline-none hover:text-inherit"
                >
                  Login <Icon icon="log-in" />
                </a>
              )}
            </li>
          </ul>
        </div>
      </Row>
    </div>
  );
};
