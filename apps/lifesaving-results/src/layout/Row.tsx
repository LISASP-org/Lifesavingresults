import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children?: ReactNode;
};

export const Row = (props: Props) => {
  return (
    <div
      className={classNames(['ml-auto mr-auto max-w-4xl w-full p-2', props.className])}
    >
      {props.children}
    </div>
  );
};
