import { GET_CANDIDATES, CANDIDATE_ERROR } from '../constants/actionTypes';
import candidateReducer from './candidateReducer';

describe('candidateReducer', () => {
  let initialState = {};
  let action = {
    type: null,
    candidates: {
      "firstName" : "TEODORA",
      "lastName" : "FLORESFOURE",
      "applicantID" : "121443402",
      "intvwStatusCode" : 19,
      "intvwResult" : "",
      "candOfferStatus" : "",
      "drugTestResult" : "",
      "activeFlag" : "N",
      "link" : { },
      "offerAccepted" : null,
      "interviewCompleted" : null,
      "statuses" : [ ]
    }
  };

  beforeEach(() => {
    action.type = null;
  });

  it('properly displays all the candidates attached to the Requisition', () => {
    action.type = GET_CANDIDATES;
    expect(candidateReducer(initialState, action))
      .toEqual({
        error: false,
        candidates: {
          "firstName" : "TEODORA",
          "lastName" : "FLORESFOURE",
          "applicantID" : "121443402",
          "intvwStatusCode" : 19,
          "intvwResult" : "",
          "candOfferStatus" : "",
          "drugTestResult" : "",
          "activeFlag" : "N",
          "link" : { },
          "offerAccepted" : null,
          "interviewCompleted" : null,
          "statuses" : [ ]
        }
      });
  });

  it('properly captures a dispatch for Candidate Error', () => {
    action.type = CANDIDATE_ERROR;
    expect(candidateReducer(initialState, action))
      .toEqual({
        error: true
      });
  });

});
