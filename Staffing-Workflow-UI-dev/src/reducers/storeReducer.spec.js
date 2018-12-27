import { IS_VALID_STORE, IS_VALID_STORE_ERROR} from '../constants/actionTypes';
import isValidStore from './storeReducer';

describe('isValidStore', () => {
  let initialState = {};

  let testActionsNeeded = {
    storeNumber: "1234",
    isValidStore: 'true'
  };

  let action = {
    type: null,
    isValidStore: testActionsNeeded
  };

  it('returns "true" if store number is valid',()=>{
    action.type = IS_VALID_STORE;
    expect(isValidStore(initialState, action))
      .toEqual({
        error: false,
        isValidStore: testActionsNeeded
      });
  });

  it('properly sets error incase of IS_VALID_STORE_ERROR', ()=>{
    action.type = IS_VALID_STORE_ERROR;
    expect(isValidStore(initialState, action))
      .toEqual({
        error: true
      });
  });
});
