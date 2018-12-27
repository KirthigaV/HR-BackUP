import { ACTIONS_NEEDED, ACTIONS_NEEDED_ERROR, ACTIONS_NEEDED_RESET } from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function getActionsNeeded(state = initialState, action) {
  let newState;

  switch (action.type) {
    case ACTIONS_NEEDED:
      newState = objectAssign({}, state);
      newState.actionsNeeded = action.actionsNeeded;
      newState.error = false;
      return newState;
    case ACTIONS_NEEDED_RESET:
      newState = objectAssign({}, state);
      newState.actionsNeeded = action.actionsNeeded;
      newState.error = false;
      return newState;
    case ACTIONS_NEEDED_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }

}
