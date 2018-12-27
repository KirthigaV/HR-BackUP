import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import { CONFIG } from '../../config/globals';
import * as CandidateActions from './candidateActions';
import {GET_CANDIDATES, CANDIDATE_ERROR, CAND_LIST_RESET} from '../constants/actionTypes';
import candidateReducer from "../reducers/candidateReducer";

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiurl;

describe('getCandidates actions',() => {
  let reqNum = 150718320;
  let initialState = {};
  let candInfo = {
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
  };

  let candInfoActon = {
    type: null,
    candidates: null
  };

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('should dispatch GET_CANDIDATES and process the api response', (done) => {
    const expectedActions = [
      {
        type: GET_CANDIDATES,
        candidates: candInfo
      }
    ];

    Moxios.stubRequest(apiURL + 'candidates/' + reqNum, {
      status: 200,
      response: candInfo
    });

    const store = mockStore({ candidates: {}});

    return store.dispatch(CandidateActions.getCandidates(reqNum)).then(() => {
      let result = store.getActions();
      expect(JSON.stringify(result)).toEqual(JSON.stringify(expectedActions));
      done();
    });
  });

  it('should dispatch REQ_ERROR on error api response', (done) => {
    Moxios.wait(() => {
      const request = Moxios.requests.mostRecent();
      request.respondWith({
        status: 500
      });
    });

    const expectedActions = [
      {
        type: CANDIDATE_ERROR
      }
    ];
    const store = mockStore();

    return store.dispatch(CandidateActions.getCandidates(reqNum)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      done();
    });
  });

  it('should reset candidates list', () => {
    candInfoActon.type = CAND_LIST_RESET;
    expect(candidateReducer(initialState, candInfoActon))
      .toEqual({
        error: false,
        candidates: null
      });
  });
});
