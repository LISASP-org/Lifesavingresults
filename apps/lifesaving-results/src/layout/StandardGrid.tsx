import type { ReactElement } from 'react';

type Props = {
  children: ReactElement | ReactElement[] | string;
};

export const StandardGrid = (props: Props) => {
  return <div className="grid grid-cols-12 gap-2">{props.children}</div>;
};
