import {GET_REQ_DETAILS, REQ_DETAILS_ERROR, REQ_INFO_RESET} from '../constants/actionTypes';
import ReqDetailsReducer from './reqDetailsReducer';

describe('ReqDetailsReducer', () => {
  let initialState = {};
  let action = {
    type: null,
    requisitionInfo: {
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
    }
  };

  let reqDetailsAction = {
    type: null,
    requisitionInfo: null
  };

  beforeEach(() => {
    action.type = null;
  });

  it('properly captures a dispatch for requisitions details', () => {
    action.type = GET_REQ_DETAILS;
    expect(ReqDetailsReducer(initialState, action))
      .toEqual({
        error: false,
        requisitionInfo: {
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
        }
      });
  });

  it('properly captures a dispatch for Req details Error', () => {
    action.type = REQ_DETAILS_ERROR;
    expect(ReqDetailsReducer(initialState, action))
      .toEqual({
        error: true
      });
  });

  it('should reset req list', () => {
    reqDetailsAction.type = REQ_INFO_RESET;
    expect(ReqDetailsReducer(initialState, reqDetailsAction))
      .toEqual({
        error: false,
        requisitionInfo: null
      });
  });

});
