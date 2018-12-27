import ConfigureMockStore from 'redux-mock-store';
import Moxios from 'moxios';
import expect from 'expect';
import Thunk from 'redux-thunk';
import { CONFIG } from '../../config/globals';
import * as StoreActions from './storeActions';
import {IS_VALID_STORE_ERROR, IS_VALID_STORE, GET_STORE_ADDRESS, STORE_ADDRESS_ERROR} from '../constants/actionTypes';

const middlewares = [Thunk];
const mockStore = ConfigureMockStore(middlewares);
const apiURL=CONFIG.apiurl;

describe('isValidStore actions',() => {
  let testStoreNum = "1234";

  let testActions = {
    storeNumber: testStoreNum,
    isValidStore: 'true'
  };

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('should dispatch IS_VALID_STORE and process the api response', (done) => {
    const expectedActions = [
      {
        type: IS_VALID_STORE,
        isValidStore: testActions
      }
    ];

    Moxios.stubRequest(apiURL + 'store/valid/' + testStoreNum, {
      status: 200,
      response: testActions
    });

    const store = mockStore({});

    return store.dispatch(StoreActions.isValidStore(testStoreNum)).then(() => {
      let result = store.getActions();
      expect(JSON.stringify(result)).toEqual(JSON.stringify(expectedActions));
      done();
    });
  });

  it('should dispatch IS_VALID_STORE_ERROR on error api response', (done) => {
    const expectedActions = [
      {
        type: IS_VALID_STORE_ERROR
      }
    ];

    Moxios.wait(() => {
      const request = Moxios.requests.mostRecent();
      request.respondWith({
        status: 500
      });
    });

    const store = mockStore({});

    return store.dispatch(StoreActions.isValidStore(testStoreNum)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      done();
    });
  });
});


describe('getStoreAddress actions',() => {
  let storeNo = 1234;
  let storeAddress = {
    "storeNo" : "0116",
    "addresLine1" : "9037 HWY 92, SUITE 100",
    "addressLine2" : "",
    "city" : "WOODSTOCK",
    "stateCode" : "GA",
    "zipcode" : "30189"
  };

  beforeEach(() => {
    Moxios.install();
  });

  afterEach(() => {
    Moxios.uninstall();
  });

  it('should dispatch GET_STORE_ADDRESS and process the api response', (done) => {
    const expectedActions = [
      {
        type: GET_STORE_ADDRESS,
        storeAddress: storeAddress
      }
    ];

    Moxios.stubRequest(apiURL + 'storeAddress/' + storeNo, {
      status: 200,
      response: storeAddress
    });

    const store = mockStore({ storeAddress: {}});

    return store.dispatch(StoreActions.getStoreAddress(storeNo)).then(() => {
      let result = store.getActions();
      expect(JSON.stringify(result)).toEqual(JSON.stringify(expectedActions));
      done();
    });
  });

  it('should dispatch STORE_ADDRESS_ERROR on error api response', (done) => {
    Moxios.wait(() => {
      const request = Moxios.requests.mostRecent();
      request.respondWith({
        status: 500
      });
    });

    const expectedActions = [
      {
        type: STORE_ADDRESS_ERROR
      }
    ];
    const store = mockStore();

    return store.dispatch(StoreActions.getStoreAddress(storeNo)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      done();
    });
  });

});
