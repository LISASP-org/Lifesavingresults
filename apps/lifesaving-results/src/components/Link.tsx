import classNames from 'classnames';
import type { ReactElement } from 'react';

type Props = {
  href: string;
  css?: string;
  children: ReactElement | ReactElement[] | string;
};

export const Link = (props: Props) => {
  return (
    <a href={props.href} className={classNames(['underline', props.css])}>
      {props.children}
    </a>
  );
};
