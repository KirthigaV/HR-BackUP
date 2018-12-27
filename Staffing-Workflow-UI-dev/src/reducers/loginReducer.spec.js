import {USER_LOGIN, USER_LOGOUT, LOGIN_ERROR, USER_PROFILE} from '../constants/actionTypes';
import loginReducer from './loginReducer';

describe('LoginReducer', () => {
    let initialState = {};
    let action = {
        type: null,
        user: {
            first_name: 'first',
            last_name: 'last',
            full_name: 'first last'
        }
    };

    beforeEach(() => {
        action.type = null;
    });

  it('properly captures a dispatch for User Login', () => {
    action.type = USER_LOGIN;
    expect(loginReducer(initialState, action))
      .toEqual({
        error: false
      });
  });

    it('properly captures a dispatch for User Profile', () => {
        action.type = USER_PROFILE;
        expect(loginReducer(initialState, action))
            .toEqual({
                error: false,
                userProfile: action.user
            });
    });

    it('properly captures a dispatch for User Logout', () => {
        action.type = USER_LOGOUT;
        expect(loginReducer(initialState, action))
            .toEqual({
                error: false,
                userProfile: null
            });
    });

    it('properly captures a dispatch for a Logout Error', () => {
        action.type = LOGIN_ERROR;
        expect(loginReducer(initialState, action))
            .toEqual({
                error: true
            });
    });
});
