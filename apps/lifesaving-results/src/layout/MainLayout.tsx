import type { ReactNode } from 'react';
import { Navigation } from './Navigation';

type Props = {
  children: ReactNode;
  title: string;
};

export const MainLayout = (props: Props) => {
  return (
    <div className="grid grid-cols-only-content lg:grid-cols-sidebar-content 2xl:grid-cols-sidebar-content-sidebar gap-2 font-sans">
      <Navigation title={props.title} />
      <div className="flex flex-col">
        <main className="flex flex-col gap-y-3">{props.children}</main>
        {/* <Footer /> */}
      </div>
    </div>
  );
};
