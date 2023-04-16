import classNames from 'classnames';
import type { ReactNode } from 'react';
import { zw } from '../Zwilling';

export const ButtonContainer = zw.div`flex flex-row flex-wrap gap-2 w-full`;
export const Spacer = zw.div`flex-grow`;

export const Button = (props: {
  className?: string;
  type: 'contained' | 'outline' | 'text';
  color: 'primary' | 'secondary';
  children?: ReactNode;
}) => {
  return (
    <button
      className={classNames([
        {
          'p-2 rounded-lg transition-all duration-200': true,
          'text-white bg-gradient-to-br from-dlrgRed-400 to-dlrgRed-500 hover:from-dlrgRed-500 hover:to-dlrgRed-600 shadow-sm':
            props.color === 'primary' && props.type !== 'text',
          'text-white bg-gradient-to-br from-dlrgGray-400 to-dlrgGray-500 hover:from-dlrgGray-500 hover:to-dlrgGray-600 shadow-sm':
            props.color === 'secondary' && props.type !== 'text',
          'text-dlrgRed-400 hover:bg-gradient-to-br hover:from-red-50  hover:to-red-100':
            props.color === 'primary' && props.type == 'text',
          'text-dlrgGray-400 hover:bg-gradient-to-br hover:from-gray-100  hover:to-gray-200':
            props.color === 'secondary' && props.type == 'text',
        },
        props.className,
      ])}
    >
      {props.children}
    </button>
  );
};
