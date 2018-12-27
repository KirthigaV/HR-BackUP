import { UPDATE_ORIENTATION, ORIENTATION_ERROR } from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function updateOrientation(state = initialState, action) {
  let newState;

  switch (action.type) {
    case UPDATE_ORIENTATION:
      newState = objectAssign({}, state);
      newState.updateSuccess = action.updateSuccess;
      newState.error = false;
      return newState;
    case ORIENTATION_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }
}
