import { UPDATE_ORIENTATION, ORIENTATION_ERROR } from '../constants/actionTypes';
import updateOrientation from './orientationReducer';

describe('orientationReducer', () => {
  let initialState = {};
  let action = {
    type: null,
    updateSuccess: true
  };

  it('properly updates orientation date for a applicant', () => {
    action.type = UPDATE_ORIENTATION;
    expect(updateOrientation(initialState, action))
      .toEqual({
        error: false,
        updateSuccess: true
      });
  });

  it('error with updating orientation date for a applicant', () => {
    action.type = ORIENTATION_ERROR;
    expect(updateOrientation(initialState, action))
      .toEqual({
        error: true
      });
  });

});
