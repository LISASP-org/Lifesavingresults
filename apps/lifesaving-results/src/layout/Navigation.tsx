import { getToken } from '../auth/jwt';
import { Link } from '../components/Link';
import { H1 } from '../components/Typography';
import { useUser } from '../hooks/auth/useUser';
import { Row } from './Grid';

const getInitials = ({
  given_name,
  family_name,
  preferred_username,
}: {
  given_name: string;
  family_name: string;
  preferred_username: string;
}) => {
  const initialActualName = [given_name, family_name]
    .filter(Boolean)
    .map((s) => s[0])
    .join('');
  if (initialActualName) return initialActualName;

  return preferred_username[0];
};

type Props = {
  title: string;
};

export const Navigation = (props: Props) => {
  const { loggedIn, name, preferred_username } = useUser();
  const token = getToken();
  const initials = token ? getInitials(token.decoded) : undefined;

  return (
    <div>
      <Row className="flex flex-row justify-between items-center p-3">
        <div className="flex flex-row items-center gap-10">
          <Link to="/" className="pt-1.5">
            <button>
              {/* <img width={120} height={19} src="/DLRGWortmarke.svg" /> */}
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={3}
                stroke="currentColor"
                className="w-8 h-8 text-dlrgRed-300"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
                />
              </svg>
            </button>
          </Link>
          <H1>{props.title}</H1>
        </div>
        <ul className="flex flex-row gap-2 text-white">
          {/* <li>
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
            </li> */}
          {!!initials && (
            <li>
              <Link to="/profile">
                <button className="w-9 h-9 text-base font-semibold bg-dlrgRed-400 rounded-full flex items-center justify-center">
                  {initials}
                </button>
              </Link>
            </li>
          )}
        </ul>
      </Row>
    </div>
  );
};
