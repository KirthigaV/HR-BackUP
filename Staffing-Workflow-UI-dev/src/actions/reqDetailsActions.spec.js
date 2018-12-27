import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import { CONFIG } from '../../config/globals';
import * as ReqDetailsAction from './reqDetailsActions';
import { GET_REQ_DETAILS, REQ_DETAILS_ERROR } from '../constants/actionTypes';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiurl;
describe('getReqDetails actions',() => {
  let storeNum = '1234';
  let reqNum = 150718320;
  let userId = "QAT2715";
  let reqInfo = {
      "requisitionNumber": reqNum,
      "storeNumber": storeNum,
      "deptNumber": "090",
      "jobTitleCode": "CASHR",
      "jobDescription": "CASHIER",
      "isSeasonal": "N",
      "createTimeStamp": "2017-11-09 11:24:01.681138",
      "createdByUserID": userId,
      "isFullTime": "N",
      "isPartTime": "Y",
      "interviewCandCount": 152,
      "positionsRequested": 150,
      "openPositions": 150,
      "isTACScheduled": "Y"
    };
  let reqAvail = {
    "reqNumber" : reqNum,
    "availability" : [ [ "Weekdays", "Saturday" ], [ "12pm-5pm" ] ]
  };
  let calendarId = {
    "calendarID" : 47,
    "calendarDescription" : "0132  - CARROLLTON,GA                                     ",
    "storeNumber" : storeNum,
    "calendarType" : "General"
  };
  let userName = {
    "userID" : userId,
    "name" : "WENDLAND,BART P",
    "storeNumber" : storeNum,
    "storeName" : "SSC INC"
  };
  let intvSched = {
    "requisitionNumber" : reqNum,
    "interviewsScheduled" : 5
  };

  const expectedResp = [{
    "type": GET_REQ_DETAILS,
    "requisitionInfo": {
      "requisitionNumber": reqNum,
      "storeNumber": storeNum,
      "deptNumber": "090",
      "jobTitleCode": "CASHR",
      "jobDescription": "CASHIER",
      "isSeasonal": "N",
      "createTimeStamp": "2017-11-09 11:24:01.681138",
      "createdByUserID": userId,
      "isFullTime": "N",
      "isPartTime": "Y",
      "interviewCandCount": 152,
      "positionsRequested": 150,
      "openPositions": 150,
      "isTACScheduled": "Y",
      "availability": [["Weekdays", "Saturday"], ["12pm-5pm"]],
      "calendar": "0132  - CARROLLTON,GA                                     ",
      "userName": "WENDLAND,BART P",
      "interviewsScheduled": 5
    }
  }];

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('getReqDetails: should dispatch GET_REQ_DETAILS and process the api response', (done) => {

    Moxios.stubRequest(apiURL + 'reqInfoList/' + storeNum, {
      status: 200,
      response: reqInfo
    });
    Moxios.stubRequest(apiURL + 'reqAvailability/' + reqNum, {
      status: 200,
      response: reqAvail
    });
    Moxios.stubRequest(apiURL + 'calInfoByReqn/' + reqNum, {
      status: 200,
      response: calendarId
    });
    Moxios.stubRequest(apiURL + 'userInfo/' + userId, {
      status: 200,
      response: userName
    });
   Moxios.stubRequest(apiURL + 'intervScheduled/' + reqNum, {
      status: 200,
      response: intvSched
    });

    const store = mockStore({ requisitionInfo: {}});

    return store.dispatch(ReqDetailsAction.getReqDetails(storeNum)).then(() => {
      let result = store.getActions();
      expect(result).toEqual(expectedResp);
      done();
    });
  });

  it('should dispatch REQ_ERROR on error api response for Req Details', (done) => {
    Moxios.wait(() => {
      const request = Moxios.requests.mostRecent();
      request.respondWith({
        status: 500
      });
    });

    const expectedActions = [
      {
        type: REQ_DETAILS_ERROR
      }
    ];
    const store = mockStore();

    return store.dispatch(ReqDetailsAction.getReqDetails(storeNum)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      done();
    });
  });
});





