import {IS_VALID_STORE, IS_VALID_STORE_ERROR, GET_STORE_ADDRESS, STORE_ADDRESS_ERROR} from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function isValidStore(state = initialState, action) {
  let newState;

  switch (action.type) {
    case IS_VALID_STORE:
      newState = objectAssign({}, state);
      newState.isValidStore = action.isValidStore;
      newState.error = false;
      return newState;
    case GET_STORE_ADDRESS:
      newState = objectAssign({}, state);
      newState.storeAddress = action.storeAddress;
      newState.error = false;
      return newState;
    case IS_VALID_STORE_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    case STORE_ADDRESS_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }

}
