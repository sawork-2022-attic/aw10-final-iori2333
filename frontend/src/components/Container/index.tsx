import React from 'react';
import './index.scss';

function Container(props: {
  className?: string;
  title?: string;
  children: React.ReactNode;
}) {
  const className =
    'pos-container' + (props.className ? ` ${props.className}` : '');

  return (
    <div className={className}>
      {props.title && <div className="title">{props.title}</div>}
      {props.children}
    </div>
  );
}

export default Container;
