import { GET_ALL_CALENDAR_DETAILS, GET_ALL_CALENDAR_DETAILS_ERROR } from '../constants/actionTypes';
import objectAssign from 'object-assign';
import initialState from './initialState';

export default function getAllCalDetails(state = initialState, action) {
  let newState;

  switch (action.type) {
    case GET_ALL_CALENDAR_DETAILS:
      newState = objectAssign({}, state);
      newState.allCalendarDetails = action.calendarDetails;
      newState.error = false;
      
      return newState;
    case GET_ALL_CALENDAR_DETAILS_ERROR:
      newState = objectAssign({}, state);
      newState.error = true;
      return newState;
    default:
      return state;
  }
}
