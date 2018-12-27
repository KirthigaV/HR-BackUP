import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import * as RequisitionActions from './requisitionActions';
import { GET_REQUISITIONS, REQ_ERROR, GET_REQ_ACTIONS } from '../constants/actionTypes';
import { CONFIG } from '../../config/globals';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiURL;

describe('getRequisitions actions',() => {
  let storeNum = '1234';
  let reqNum = 150718320;
  let reqInfoList = {
    "requisitionNumber": reqNum,
    "storeNumber": storeNum,
    "deptNumber": "090",
    "jobTitleCode": "CASHR",
    "jobDescription": "CASHIER",
    "isSeasonal": "N",
    "createTimeStamp": "2017-11-09 11:24:01.681138",
    "createdByUserID": "QAT2715",
    "isFullTime": "N",
    "isPartTime": "Y",
    "interviewCandCount": 152,
    "positionsRequested": 150,
    "openPositions": 150,
    "isTACScheduled": "Y"
  };
  let reqInfo = {
    "storeNumber" : storeNum,
    "reqInfoList" : [reqInfoList]
  };
  let reqAvail = {
    "reqNumber" : reqNum,
    "availability" : [ [ "Weekdays", "Saturday" ], [ "12pm-5pm" ] ]
  };
  let actionsNeeded = {
    "requisitionNumber" : reqNum,
    "actionsNeeded" : 1
  };

  beforeEach(() => {
    Moxios.install();

  });

  afterEach(() => {
    Moxios.uninstall();
  });

    it.skip('Should dispatch GET_REQUISITIONS and process the api response', (done) => {
      let copyReqList = Object.assign({},reqInfoList);
      copyReqList.availPref = Object.assign([],reqAvail.availability);
      let expectedReqInfo = [copyReqList];
      const expectedActions = [
        {
          type: GET_REQUISITIONS,
          requisitionInfo: expectedReqInfo
        }
      ];

      Moxios.stubRequest(apiURL + 'infoList/' + storeNum, {
        status: 200,
        response: reqInfo
      });
      Moxios.stubRequest(apiURL + 'reqAvailability/' + reqNum, {
        status: 200,
        response: reqAvail
      });
      const store = mockStore({ requisitionInfo: {}});

      return store.dispatch(RequisitionActions.getRequisitions(storeNum)).then(() => {
        let result = store.getActions();
        expect(result).toEqual(expectedActions);
        done();
      });
    });

    it.skip("Should dispatch GET_REQ_ACTIONS", (done) => {
    let copyReqList = Object.assign({},reqInfoList);
    copyReqList.availPref = Object.assign([],reqAvail.availability);
    let reqInfo = [copyReqList];
    const expectedActions = [
      {
        type: GET_REQ_ACTIONS,
        requisitionActions: [
          {
            "requisitionNumber" : reqNum,
            "actionsNeeded" : 1
          }
        ]
      }
    ];

    Moxios.stubRequest(apiURL + 'reqActions/' + reqNum, {
      status: 200,
      response: actionsNeeded
    });

    const store = mockStore({ requisitionActions: {}});
    return store.dispatch(RequisitionActions.getReqActionsNeeded(reqInfo)).then(() => {
      let result = store.getActions();
      expect(result).toEqual(expectedActions);
      done();
    });
  });

    it.skip('Should dispatch REQ_ERROR on error api response', (done) => {
      Moxios.wait(() => {
        const request = Moxios.requests.mostRecent();
        request.respondWith({
          status: 500
        });
      });

      const expectedActions = [
        {
          type: REQ_ERROR
        }
      ];
      const store = mockStore();

      return store.dispatch(RequisitionActions.getRequisitions(storeNum)).then(() => {
          expect(store.getActions()).toEqual(expectedActions);
          done();
        });
      });
    });
