import React from 'react';
import './index.scss';

export interface TextButtonProps {
  text?: string;
  className?: string;
  icon?: string;
  onClick?: () => void;
}

function TextButton(props: TextButtonProps) {
  const className = props.className
    ? props.className + ' text-btn'
    : 'text-btn';
  return (
    <button className={className} onClick={props.onClick}>
      {props.icon && <div className={`codicon codicon-${props.icon}`} />}
      <span>{props.text}</span>
    </button>
  );
}

export default React.memo(TextButton);
