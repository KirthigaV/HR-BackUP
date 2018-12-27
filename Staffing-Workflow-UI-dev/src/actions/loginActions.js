import { CONFIG } from 'globals';
import fetch from 'isomorphic-fetch';
import { LOGIN_ERROR, USER_PROFILE, USER_LOGOUT} from '../constants/actionTypes';


export const loginUser = (storeNumber, username, password) => {

  return dispatch => {
    return fetch(CONFIG.authUrl + '/ssoLogin', {
      credentials: 'include',
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        callingProgram: CONFIG.projectInfo.appName,
        j_storenumber: storeNumber,
        j_username: username,
        j_password: password
      })
    })
      .then(response => {
        if (response.ok) {
          dispatch(getUserInfo(username));
        } else {
          return dispatch({
            type: LOGIN_ERROR
          });
        }
      })
      .catch((error) => {
        console.error("login error", error);
      });
  };
};

const getUserInfo = (username) => {

  return dispatch => {
    return fetch(CONFIG.authUrl + '/getUserInfo?username=' + username, {
      credentials: 'include',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    })
      .then(response => {
        if (response.ok) {
          window.location.replace('/home');
        } else {
          return dispatch({
            type: LOGIN_ERROR
          });
        }
      })
      .catch((error) => {
        console.error("getUserInfo error", error);
      });
  };
};

export const setUserProfile = (userProfile) => {

    return dispatch => {
      dispatch({
        type: USER_PROFILE,
        user: userProfile
      });
  };
};

export const logoutUser = () => {

  return dispatch => {
    dispatch({
      type: USER_LOGOUT
    });
  };
};

export const logoutUserSM = () => {

  return dispatch => {
    if (CONFIG.authUrl === '')
      window.location = '/logout';
    else {
      window.location = CONFIG.authUrl + '/logout';
    }
    dispatch({
      type: USER_LOGOUT
    });
  };
};
