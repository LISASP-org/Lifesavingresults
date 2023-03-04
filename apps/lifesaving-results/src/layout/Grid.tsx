import type { ReactNode } from 'react';
import { zw } from '../Zwilling';

export const Row = zw.div`ml-auto mr-auto max-w-4xl w-full md:pl-3 md:pr-3`;

export const StandardGrid = zw.div`grid grid-cols-12 gap-3`;

export const TwoColumns = (props: {
  className?: string;
  children: [ReactNode, ReactNode];
}) => {
  return (
    <StandardGrid className={props.className}>
      <div className="col-span-12 md:col-span-6">{props.children[0]}</div>
      <div className="col-span-12 md:col-span-6">{props.children[1]}</div>
    </StandardGrid>
  );
};

export const ThreeColumns = (props: {
  className?: string;
  children: [ReactNode, ReactNode, ReactNode];
}) => {
  return (
    <StandardGrid className={props.className}>
      <div className="col-span-12 md:col-span-4">{props.children[0]}</div>
      <div className="col-span-12 md:col-span-4">{props.children[1]}</div>
      <div className="col-span-12 md:col-span-4">{props.children[2]}</div>
    </StandardGrid>
  );
};
