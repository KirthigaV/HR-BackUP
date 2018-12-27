import {
  GET_REQ_ACTIONS, GET_REQUISITIONS, REQ_ACTIONS_RESET, REQ_ERROR
} from '../constants/actionTypes';
import requisitionReducer from './requisitionReducer';

describe('RequisitionReducer', () => {
  let initialState = {};
  let reqInfoAction = {
    type: null,
    requisitionInfo: {
      "storeNumber": "0132",
      "reqInfoList": [{
        "requisitionNumber": 150718320,
        "storeNumber": "0132",
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
      }]
    }
  };

  let reqActionsAction = {
    type: null,
    requisitionActions: [{
      "requisitionNumber": "0123",
      "actionsNeeded": 1
    }]
  };

  let reqActions = {
    type: null,
    requisitionActions: null
  };

  beforeEach(() => {
    reqInfoAction.type = null;
    reqActionsAction.type = null;
  });

  it('properly captures a dispatch for get requisitions', () => {
    reqInfoAction.type = GET_REQUISITIONS;
    expect(requisitionReducer(initialState, reqInfoAction))
      .toEqual({
        error: false,
        requisitionInfo: {
          "storeNumber" : "0132",
          "reqInfoList" : [ {
            "requisitionNumber" : 150718320,
            "storeNumber" : "0132",
            "deptNumber" : "090",
            "jobTitleCode" : "CASHR",
            "jobDescription" : "CASHIER",
            "isSeasonal" : "N",
            "createTimeStamp" : "2017-11-09 11:24:01.681138",
            "createdByUserID" : "QAT2715",
            "isFullTime" : "N",
            "isPartTime" : "Y",
            "interviewCandCount" : 152,
            "positionsRequested" : 150,
            "openPositions" : 150,
            "isTACScheduled" : "Y"
          } ]
        }
      });
  });

  it('properly captures a dispatch for get requisition actions', () => {
    reqActionsAction.type = GET_REQ_ACTIONS;
    expect(requisitionReducer(initialState, reqActionsAction))
      .toEqual({
        error: false,
        requisitionActions: [
          {
            "requisitionNumber" : "0123",
            "actionsNeeded" : 1
          }
        ]
      });
  });

  it('properly captures a dispatch for Req Error', () => {
    reqInfoAction.type = REQ_ERROR;
    expect(requisitionReducer(initialState, reqInfoAction))
      .toEqual({
        error: true
      });
  });

  it('should reset req list', () => {
    reqActions.type = REQ_ACTIONS_RESET;
    expect(requisitionReducer(initialState, reqActions))
      .toEqual({
        error: false,
        requisitionActions: null
      });
  });
});
