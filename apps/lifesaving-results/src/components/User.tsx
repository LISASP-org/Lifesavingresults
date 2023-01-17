import classNames from 'classnames';
import { useUserQuery } from '../hooks/queries/useUserQuery';

type Props = {
  css?: string;
};

export const User = (props: Props) => {
  const { data: user, isLoading, error } = useUserQuery({ userId: '6' });

  return (
    <div className={classNames(['', props.css])}>
      {!!isLoading && <span>Loading user from REST API...</span>}
      {!!user && (
        <span>
          Hello {[user.data.first_name, user.data.last_name].filter(Boolean).join(' ')}!
        </span>
      )}
    </div>
  );
};
