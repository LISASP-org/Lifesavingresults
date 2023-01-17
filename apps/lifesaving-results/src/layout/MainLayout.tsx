import type { ReactElement } from 'react';
import { Footer } from './Footer';
import { Navigation } from './Navigation';

type Props = {
  children: ReactElement | ReactElement[] | string;
};

export const MainLayout = (props: Props) => {
  return (
    <div className="flex flex-col h-full gap-2">
      <Navigation />
      <div className="flex flex-col">{props.children}</div>
      <Footer />
    </div>
  );
};
