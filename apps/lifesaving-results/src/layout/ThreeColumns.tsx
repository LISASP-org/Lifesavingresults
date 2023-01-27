import type { ReactNode } from 'react';
import { StandardGrid } from './StandardGrid';

type Props = {
  className?: string;
  children: [ReactNode, ReactNode, ReactNode];
};

export const ThreeColumns = (props: Props) => {
  return (
    <StandardGrid className={props.className}>
      <div className="col-span-12 md:col-span-4">{props.children[0]}</div>
      <div className="col-span-12 md:col-span-4">{props.children[1]}</div>
      <div className="col-span-12 md:col-span-4">{props.children[2]}</div>
    </StandardGrid>
  );
};
