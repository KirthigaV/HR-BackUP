import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import { CONFIG } from '../../config/globals';
import * as CalendarActions from './calendarActions';
import {GET_ALL_CALENDAR_DETAILS, GET_ALL_CALENDAR_DETAILS_ERROR} from '../constants/actionTypes';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiurl;

describe('getAllCalendarDetails actions',() => {
  let storeNum = '1234';
  let calendarDetailsList = {
      "calendarID": 572,
      "calendarDescription": "1234  - N BUFFALO",
      "reqCount": 0,
      "slotsAvailable": 0
  };

  const expectedResp = [{
    "type": GET_ALL_CALENDAR_DETAILS,
    "calendarDetails": {
      "calendarID": 572,
      "calendarDescription": "1234  - N BUFFALO",
      "reqCount": 0,
      "slotsAvailable": 0
    }
  }];

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

    it('should dispatch GET_ALL_CALENDAR_DETAILS and process the api response', (done) => {
      Moxios.stubRequest(apiURL + 'calInfoByStore/' + storeNum, {
        status: 200,
        response: calendarDetailsList
      });

      const store = mockStore({ calendarDetails: {}});

      return store.dispatch(CalendarActions.getAllCalendarDetails(storeNum)).then(() => {
        let result = store.getActions();
        expect(result).toEqual(expectedResp);
        done();
      });
    });

    it('should dispatch GET_ALL_CALENDAR_DETAILS_ERROR on error api response', (done) => {
      Moxios.wait(() => {
        const request = Moxios.requests.mostRecent();
        request.respondWith({
          status: 500
        });
      });

      const expectedActions = [
        {
          type: GET_ALL_CALENDAR_DETAILS_ERROR
        }
      ];
      const store = mockStore();

      return store.dispatch(CalendarActions.getAllCalendarDetails(storeNum)).then(() => {
          expect(store.getActions()).toEqual(expectedActions);
          done();
        });
      });
    });
