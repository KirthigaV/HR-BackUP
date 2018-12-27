import { GET_REQ_DETAILS, REQ_DETAILS_ERROR, REQ_INFO_RESET } from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function getReqDetails(state = initialState, action) {
  let newState;

  switch (action.type) {
    case GET_REQ_DETAILS:
    case REQ_INFO_RESET:
      newState = objectAssign({}, state);
      newState.requisitionInfo = action.requisitionInfo;
      newState.error = false;
      return newState;
    case REQ_DETAILS_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }
}
