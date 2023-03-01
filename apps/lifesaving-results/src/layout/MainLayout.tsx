import type { ReactNode } from 'react';
import { Navigation } from './Navigation';

type Props = {
  children: ReactNode;
  title: string;
};

export const MainLayout = (props: Props) => {
  return (
    <div className="flex flex-col h-full gap-2 font-sans">
      <Navigation title={props.title} />
      <div className="flex flex-col gap-y-3">{props.children}</div>
      {/* <Footer /> */}
    </div>
  );
};
