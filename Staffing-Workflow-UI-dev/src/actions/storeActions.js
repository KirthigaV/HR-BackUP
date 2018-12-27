import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import {IS_VALID_STORE, IS_VALID_STORE_ERROR, GET_STORE_ADDRESS, STORE_ADDRESS_ERROR} from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;

export const isValidStore = (storeNumber) => {
  return dispatch => {
    return Axios.get(apiURL + 'store/valid/' + storeNumber,{headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch({
          type:IS_VALID_STORE,
          isValidStore: response.data
        });
      })
      .catch(() => {
        return dispatch({
          type: IS_VALID_STORE_ERROR
        });
      });
  };
};

export const getStoreAddress = (storeNo) => {

  return dispatch => {
    return Axios.get(apiURL + 'storeAddress/' + storeNo, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch({
          type: GET_STORE_ADDRESS,
          storeAddress: response.data
        });
      })
      .catch(() => {
        return dispatch({
          type: STORE_ADDRESS_ERROR
        });
      });
  };
};
