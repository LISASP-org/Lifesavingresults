import type { MakeLinkPropsOptions, RegisteredRoutesInfo } from '@tanstack/react-router';
import { Link as RouterLink } from '@tanstack/react-router';
import classNames from 'classnames';

export const Link = <
  TDefaultFrom extends RegisteredRoutesInfo['routePaths'] = '/',
  TDefaultTo extends string = '.'
>(
  props: MakeLinkPropsOptions<TDefaultFrom, TDefaultTo>
) => {
  return (
    // @ts-ignore
    <RouterLink
      {...props}
      className={classNames([
        'underline outline-none hover:text-inherit',
        props.className,
      ])}
    >
      {props.children}
    </RouterLink>
  );
};
