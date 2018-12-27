import { ACTIONS_NEEDED, ACTIONS_NEEDED_ERROR} from '../constants/actionTypes';
import getActionsNeeded from './actionsNeededReducer';

describe('getActionsNeeded', () => {
  let initialState = {};

  let testActionsNeeded = {
    storeNumber: "1234",
    actionsNeeded: 1
  };

  let action = {
    type: null,
    actionsNeeded: testActionsNeeded
  };

  it('properly returns total actions for a store',()=>{
    action.type = ACTIONS_NEEDED;
    expect(getActionsNeeded(initialState, action))
      .toEqual({
        error: false,
        actionsNeeded: testActionsNeeded
      });
  });

  it('properly sets error incase of ACTIONS_NEEDED_ERROR', ()=>{
    action.type = ACTIONS_NEEDED_ERROR;
    expect(getActionsNeeded(initialState, action))
      .toEqual({
        error: true
      });
  });
});
