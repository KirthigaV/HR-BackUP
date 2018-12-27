import {GET_REQUISITIONS, REQ_ERROR, GET_REQ_ACTIONS, REQ_ACTIONS_RESET} from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function getRequisitions(state = initialState, action) {
  let newState;

  switch (action.type) {
    case GET_REQUISITIONS:
      newState = objectAssign({}, state);
      newState.requisitionInfo = action.requisitionInfo;
      newState.error = false;
      return newState;

    case GET_REQ_ACTIONS:
    case REQ_ACTIONS_RESET:
      newState = objectAssign({}, state);
      newState.requisitionActions = action.requisitionActions;
      newState.error = false;
      return newState;

    case REQ_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;

    default:
      return state;
  }
}
