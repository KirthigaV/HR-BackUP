import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import Cookie from 'react-cookie';
import {GET_ALL_CALENDAR_DETAILS, GET_ALL_CALENDAR_DETAILS_ERROR} from "../constants/actionTypes";
const apiURL=CONFIG.apiurl;

export const getAllCalendarDetails = (storeNo) => {

  return dispatch => {
    return Axios.get(apiURL + 'calInfoByStore/' + storeNo, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch({
          type: GET_ALL_CALENDAR_DETAILS,
          calendarDetails: response.data
        });
      })
      .catch(() => {
        return dispatch({
          type:GET_ALL_CALENDAR_DETAILS_ERROR
        });}
      );
  };
};
