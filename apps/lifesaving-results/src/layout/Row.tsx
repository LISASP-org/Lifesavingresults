import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children?: ReactNode;
};

export const Row = (props: Props) => {
  return (
    <div
      className={classNames([
        'ml-auto mr-auto max-w-4xl w-full md:pl-3 md:pr-3',
        props.className,
      ])}
    >
      {props.children}
    </div>
  );
};
