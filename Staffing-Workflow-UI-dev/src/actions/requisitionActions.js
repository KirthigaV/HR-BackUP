import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import {GET_REQUISITIONS, REQ_ERROR, GET_REQ_ACTIONS, REQ_ACTIONS_RESET} from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;

export const getRequisitions = (storeNumber) => {

  return dispatch => {
    return Axios.get(apiURL + 'infoList/' + storeNumber,  {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
        .then(function (response) {
        return dispatch(getAvailability(response.data.reqInfoList));
      })
      .catch(() => {
        return dispatch({
          type:REQ_ERROR
        });}
      );
  };
};

const getAvailability = (requisitions) => {

  return dispatch => {
    let newReq = {};
    let reqList = [];
    let promises = [];
    for(let req of requisitions){
      promises.push(Axios.get(apiURL + 'reqAvailability/' + req.requisitionNumber, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
          'api_key': CONFIG.apikey}}));
    }
    return Promise.all(promises).then(function (response) {
      for (let req of requisitions){
        let foundItem = response.find(function(item) {
          return item.data.reqNumber === req.requisitionNumber;
        });
        newReq = req;
        newReq.availPref = foundItem.data.availability;
        reqList.push(newReq);
      }
      return dispatch({
        type: GET_REQUISITIONS,
        requisitionInfo: reqList
      });
    });
  };
};

export const getReqActionsNeeded = (requisitions) => {

  return dispatch => {
    let reqList = [];
    let promises = [];
    for(let req of requisitions){
      promises.push(Axios.get(apiURL + 'reqActions/' + req.requisitionNumber,{headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
          'api_key': CONFIG.apikey}}));
    }
    return Promise.all(promises).then(function (response) {
      for (let req of requisitions){
        let foundItem = response.find(function(item) {
          return item.data.requisitionNumber === req.requisitionNumber;
        });
        let reqActions = foundItem.data;
        reqList.push(reqActions);
      }
      reqList.sort(function(a, b){
        return b.actionsNeeded-a.actionsNeeded;
      });
      return dispatch({
        type: GET_REQ_ACTIONS,
        requisitionActions: reqList
      });
    });
  };
};

export const resetRequisitionsList = () => {
  return dispatch => {
    dispatch({
      type: REQ_ACTIONS_RESET,
      requisitionActions: null,
    });
  };
};

