import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import {GET_REQ_DETAILS, REQ_INFO_RESET, REQ_DETAILS_ERROR} from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;
export const getReqDetails = (requisitionNumber) => {

  return dispatch => {
    return Axios.get(apiURL + 'reqInfoList/' + requisitionNumber, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch(getAvailability(response.data));
      })
      .catch(() => {
        return dispatch({
          type:REQ_DETAILS_ERROR
        });}
      );
  };
};

const getAvailability = (requisition) => {

  return dispatch => {
    let newReq;
    return Axios.get(apiURL + 'reqAvailability/' + requisition.requisitionNumber, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then((availability) => {
        newReq = requisition;
        newReq.availability = availability.data.availability;
        return dispatch(getSelectedCalendar(newReq));
      })
      .catch(() => {
        return dispatch({
          type:REQ_DETAILS_ERROR
        });}
      );
  };
};

const getSelectedCalendar = (requisition) => {

  return dispatch => {
    let newReq;
    return Axios.get(apiURL + 'calInfoByReqn/' + requisition.requisitionNumber, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then((calendarInfo) => {
      newReq = requisition;
      newReq.calendar = calendarInfo.data.calendarDescription;
      return dispatch(getUserName(newReq));
    });
  };
};

const getUserName = (requisition) => {
  return dispatch =>{
    let newReq;
    return Axios.get(apiURL + 'userInfo/' + requisition.createdByUserID,{headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then((userName) =>{
          newReq = requisition;
          newReq.userName = userName.data.name;
          return dispatch(getInterviewsScheduled(newReq));
        }
      );
  };
};

const getInterviewsScheduled = (requisition) => {
  return dispatch =>{
    let newReq;
    return Axios.get(apiURL + 'intervScheduled/' + requisition.requisitionNumber,{headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then((intvScheduledResponse) =>{
          newReq = requisition;
          newReq.interviewsScheduled = intvScheduledResponse.data.interviewsScheduled;
          return dispatch({
            type: GET_REQ_DETAILS,
            requisitionInfo: newReq
          });
        }
      );
  };
};

export const resetRequisitionsInfo = () => {

  return dispatch => {
    dispatch({
      type: REQ_INFO_RESET,
      requisitionInfo: null
    });
  };
};

