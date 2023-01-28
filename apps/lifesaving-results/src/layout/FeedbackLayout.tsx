import classNames from 'classnames';
import type { ReactNode } from 'react';

type Props = {
  className?: string;
  children: ReactNode;
};

export const FeedbackLayout = (props: Props) => {
  return (
    <div
      className={classNames([
        'min-h-screen min-w-full',
        'flex p-5 items-start justify-center',
        "bg-[url('/wave-haikei.svg')] bg-cover",
        props.className,
      ])}
    >
      {props.children}
    </div>
  );
};
