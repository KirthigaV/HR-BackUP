import React from 'react';

export function showLoadingBar(flag) {
  if(flag)
    return <div className="progress-linear indeterminate" />;
  else
    return <div/>;
}

