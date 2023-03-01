import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children: ReactNode;
};

export const H1 = (props: Props) => {
  return (
    <h1 className={classNames(['text-dlrgRed-400 text-xl', props.className])}>
      {props.children}
    </h1>
  );
};

export const H2 = (props: Props) => {
  return <h2 className={classNames(['', props.className])}>{props.children}</h2>;
};

export const H3 = (props: Props) => {
  return <h3 className={classNames(['', props.className])}>{props.children}</h3>;
};

export const H4 = (props: Props) => {
  return <h4 className={classNames(['', props.className])}>{props.children}</h4>;
};
