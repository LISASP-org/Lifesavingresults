import classNames from 'classnames';
import type { ReactElement } from 'react';

type Props = {
  css?: string;
  children?: ReactElement | ReactElement[] | boolean | string;
};

export const Row = (props: Props) => {
  return (
    <div className={classNames(['ml-auto mr-auto max-w-4xl w-full p-2', props.css])}>
      {props.children}
    </div>
  );
};
