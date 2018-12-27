import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import { CONFIG } from '../../config/globals';
import * as ActionsNeededActions from './actionsNeededActions';
import { ACTIONS_NEEDED_ERROR, ACTIONS_NEEDED } from '../constants/actionTypes';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiurl;
describe('getStoreActionsNeeded actions',() => {
  let testStoreNum = "1234";

  let testActions = {
    storeNumber: testStoreNum,
    actionsNeeded: 1
  };

  beforeEach(() => {
     Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('should dispatch ACTIONS_NEEDED and process the api response', (done) => {

    const expectedActions = [
      {
        type: ACTIONS_NEEDED,
        actionsNeeded: testActions
      }
    ];

    Moxios.stubRequest(apiURL + 'storeActions/' + testStoreNum, {
      status: 200,
      response: testActions
    });

    const store = mockStore({});

    return store.dispatch(ActionsNeededActions.getStoreActionsNeeded(testStoreNum)).then(() => {
      let result = store.getActions();
      expect(JSON.stringify(result)).toEqual(JSON.stringify(expectedActions));
      done();
    });
  });

  it('should dispatch ACTIONS_NEEDED_ERROR on error api response', (done) => {
    const expectedActions = [
      {
        type: ACTIONS_NEEDED_ERROR
      }
    ];

    Moxios.wait(() => {
      const request = Moxios.requests.mostRecent();
      request.respondWith({
        status: 500
      });
    });

    const store = mockStore({});

    return store.dispatch(ActionsNeededActions.getStoreActionsNeeded(testStoreNum)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      done();
    });
  });
});
