import classNames from 'classnames';
import type { ReactNode } from 'react';
import { HR } from './HR';
import { H1 } from './Typography';

type Props = {
  title?: string;
  className?: string;
  children?: ReactNode;
};

export const Card = (props: Props) => {
  return (
    <div
      className={classNames([
        'md:rounded-xl shadow-md p-5 border-t border-b md:border',
        props.className,
      ])}
    >
      {!!props.title && (
        <div className="mb-4">
          <H1>{props.title}</H1>
          <HR />
        </div>
      )}
      {props.children}
    </div>
  );
};
