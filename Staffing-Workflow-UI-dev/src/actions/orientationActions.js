import { CONFIG } from '../../config/globals';
import { UPDATE_ORIENTATION, ORIENTATION_ERROR } from '../constants/actionTypes';
import Cookie from 'react-cookie';
const apiURL=CONFIG.apiurl;
export const updateOrientation = (orientation) => {

  return dispatch => {
    let updateSuccess = false;

    return fetch(apiURL + 'orientation/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': Cookie.load('THDSSO'),
        'api_key': CONFIG.apikey
      },
      body: JSON.stringify({
        applicantId: orientation.applicantId,
        requisitionNumber: orientation.requisitionNumber,
        orientationDate: orientation.orientationDate,
        orientationTime: orientation.orientationTime,
        userId: orientation.username,
        storeNo: orientation.storeNo,
        firstName: orientation.firstName,
        jobTitleDescription:orientation.jobDescription,
        emailAddress: orientation.email,
        addressLine1: orientation.addressLine1,
        addressLine2: orientation.addressLine2,
        city: orientation.city,
        state: orientation.stateCode,
        zipCode: orientation.zipCode,
        phoneNumber: orientation.locationPhoneNo
      })
    })
      .then(function (response) {
        if(response.ok || response.data === 1) {
          updateSuccess = true;
        }
        return dispatch({
          type: UPDATE_ORIENTATION,
          updateSuccess: updateSuccess
        });
      })
      .catch(() => {
        return dispatch({
          type: ORIENTATION_ERROR
        });
      });
  };
};
