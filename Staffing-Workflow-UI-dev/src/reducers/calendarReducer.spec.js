import { GET_ALL_CALENDAR_DETAILS, GET_ALL_CALENDAR_DETAILS_ERROR } from '../constants/actionTypes';
import CalendarReducer from './calendarReducer';

describe('CalendarReducer', () => {
  let initialState = {};
  let action = {
    type: null,
    calendarDetails: {
      "calendarID": 30,
      "calendarDescription": "0111  - MERCHANTS WALK",
      "reqCount": 46,
      "slotsAvailable": 0,
      "slotsScheduled": 0,
    }
  };

  beforeEach(() => {
    action.type = null;
  });

  it('properly captures a dispatch for all calendar details', () => {
    action.type = GET_ALL_CALENDAR_DETAILS;
    expect(CalendarReducer(initialState, action))
      .toEqual({
        error: false,
        allCalendarDetails: {
          "calendarID": 30,
          "calendarDescription": "0111  - MERCHANTS WALK",
          "reqCount": 46,
          "slotsAvailable": 0,
          "slotsScheduled":0
        }
      });
  });

  it('properly captures a dispatch for all calendar details error', () => {
    action.type = GET_ALL_CALENDAR_DETAILS_ERROR;
    expect(CalendarReducer(initialState, action))
      .toEqual({
        error: true
      });
  });
});
