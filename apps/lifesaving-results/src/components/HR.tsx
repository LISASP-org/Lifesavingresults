import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children?: ReactNode;
};

export const HR = (props: Props) => {
  return <hr className={classNames(['', props.className])}>{props.children}</hr>;
};
