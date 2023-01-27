import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children: ReactNode;
};

export const StandardGrid = (props: Props) => {
  return (
    <div className={classNames(['grid grid-cols-12 gap-2', props.className])}>
      {props.children}
    </div>
  );
};
