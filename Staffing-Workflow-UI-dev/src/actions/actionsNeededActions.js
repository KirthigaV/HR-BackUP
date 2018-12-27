import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import { ACTIONS_NEEDED_ERROR, ACTIONS_NEEDED, ACTIONS_NEEDED_RESET } from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;

export const getStoreActionsNeeded = (storeNumber) => {
  return dispatch => {
    return Axios.get(apiURL + 'storeActions/' + storeNumber,{headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch({
          type:ACTIONS_NEEDED,
          actionsNeeded: response.data
        });
      })
      .catch(() => {
        return dispatch({
          type: ACTIONS_NEEDED_ERROR
        });
    });
  };
};

export const resetActionsNeeded = () => {

  return dispatch => {
    dispatch({
      type: ACTIONS_NEEDED_RESET,
      actionsNeeded: null
    });
  };
};
