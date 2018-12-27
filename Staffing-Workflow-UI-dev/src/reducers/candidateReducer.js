import { GET_CANDIDATES, CANDIDATE_ERROR, CAND_LIST_RESET } from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function getCandidates(state = initialState, action) {
  let newState;

  switch (action.type) {
    case GET_CANDIDATES:
    case CAND_LIST_RESET:
      newState = objectAssign({}, state);
      newState.candidates = action.candidates;
      newState.error = false;
      return newState;
    case CANDIDATE_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }
}
