import type { IconName, Intent } from '@blueprintjs/core';
import { Button as BlueprintButton } from '@blueprintjs/core';

import classNames from 'classnames';

type Props = {
  css?: string;
  text: string;
  intent?: Intent;
  icon?: IconName;
};

export const Button = (props: Props) => {
  return (
    <BlueprintButton
      className={classNames(['', props.css])}
      text={props.text}
      intent={props.intent}
      icon={props.icon}
      outlined={false}
    />
  );
};
