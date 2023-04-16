import { Link } from '../components/Link';
import { useUser } from '../hooks/auth/useUser';

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

const navigationEntries: { name: string; link: any }[] = [
  {
    name: 'Dashboard',
    link: '/',
  },
  {
    name: 'Competitions',
    link: '/competition',
  },
  {
    name: 'Profile',
    link: '/profile',
  },
];

type Props = {
  title: string;
};

export const Navigation = (props: Props) => {
  const { loggedIn, name, preferred_username } = useUser();

  return (
    <nav className="sticky flex flex-col lg:h-screen top-0 lg:bottom-0 shadow lg:shadow-none pt-2 lg:pt-5 pb-2 lg:pb-5 p-5 bg-white">
      <div className="flex flex-col gap-2 lg:gap-5">
        <span className="font-rampart text-dlrgRed-400 text-2xl lg:text-4xl">
          Lifesaving
        </span>
        <ul className="flex flex-row gap-2 lg:flex-col lg:text-lg">
          {navigationEntries.map((entry) => (
            <Link key={entry.name} to={entry.link}>
              <li className="rounded-lg -ml-2 p-2 hover:bg-red-100 cursor-pointer">
                {entry.name}
              </li>
            </Link>
          ))}
        </ul>
      </div>
    </nav>
  );
};
