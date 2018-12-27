import Axios from 'axios';
import { CONFIG } from '../../config/globals';
import { GET_CANDIDATES, CANDIDATE_ERROR, CAND_LIST_RESET } from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;

export const getCandidates = (requisitionNumber) => {

  return dispatch => {
    return Axios.get(apiURL + 'candidates/' + requisitionNumber, {headers: { "Content-type":"application/json",
        "Authorization": Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey}})
      .then(function (response) {
        return dispatch({
          type: GET_CANDIDATES,
          candidates: response.data
        });
      })
      .catch(() => {
        return dispatch({
          type: CANDIDATE_ERROR
        });
    });
  };
};

export const resetCandidates = () => {
  return dispatch => {
    dispatch({
      type: CAND_LIST_RESET,
      candidates: null
    });
  };
};
