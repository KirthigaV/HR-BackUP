import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import * as OrientationActions from './orientationActions';
import { UPDATE_ORIENTATION, ORIENTATION_ERROR } from '../constants/actionTypes';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);


describe('getCandidates actions',() => {
  let reqNum = 150718320;
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
  let userId = 'nxk1313';
  let orientationDate = '2018-01-29';
  let orientationTime = '09:00 am';

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('should dispatch UPDATE_ORIENTATION and process the api response', (done) => {

    fetch.mockResponse(JSON.stringify({"type":"UPDATE_ORIENTATION","updateSuccess":true}), {status : 200, response : 1} );
    const expectedActions = [
      {
        type: UPDATE_ORIENTATION,
        updateSuccess: true
      }
    ];
    const store = mockStore({});

    return store.dispatch(OrientationActions.updateOrientation(candInfo.applicantID, reqNum,
      orientationDate, orientationTime, userId)).then(() => {
        const result = store.getActions();
        expect(JSON.stringify(result)).toEqual(JSON.stringify(expectedActions));
        done();
    });
  });

  it('should dispatch ORIENTATION_ERROR on error api response', (done) => {

    fetch.mockReject(new Error('fake error message'));

    const expectedActions = [
      { type: ORIENTATION_ERROR}
    ];
    const store = mockStore();

    return store.dispatch(OrientationActions.updateOrientation(candInfo.applicantID, reqNum,
      orientationDate, orientationTime, userId)).then(() => {
        expect(store.getActions()).toEqual(expectedActions);
        done();
    });
  });
});
