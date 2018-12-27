import Cookies from 'react-cookie';
import {CONFIG} from 'globals';

export default class Authentication {

  static isAuthenticated() {
    let thdsso = Cookies.load('THDSSO');
    if (thdsso) {
      return this.checkIsSessionValid()
        .then((response) => {
          let isValid = response.Valid;
          return isValid;
        });
    } else {
      this.clearSession();
      return Promise.resolve(false);
    }
  }

  static checkIsSessionValid() {

    return fetch(CONFIG.authUrl + '/isSessionValid', {
      credentials: 'include',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    })
      .then(response => response.json())
      .then((json) => {
        return json;
      })
      .catch((error) => {
        console.log("isSessionValid error", error);
        this.clearSession();
        let isValid = {"Valid":"false"};
        return isValid;
      });
  }

  static clearSession() {
    Cookies.remove('THDSSO', {'domain': '.homedepot.com'});
    Cookies.remove('username', {'domain': '.homedepot.com'});
    Cookies.remove('userInfo', {'domain': '.homedepot.com'});
    //Cookies.remove('access_token', {'domain': '.homedepot.com'});
    let c = document.cookie.split("; ");
    for (let i in c) {
      if (/^[^=]+/.exec(c[i]) !== null)
        document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
  }


  static getUserDetails = () => {

    return fetch(CONFIG.authUrl + '/getUserProfile', {
      credentials: 'include',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    }).then(response => response.json())
      .then((json) => {
        return json;
      });
  };

  static getUserProfile(state) {
    let userProfile = null;
    return new Promise((resolve, reject) => {

      if (state !== undefined && state.userProfile) {
        userProfile = state.userProfile;
        resolve(userProfile);
      } else if (typeof Cookies.load('userInfo') !== 'undefined' || Cookies.load('userInfo') != null) {
        let userInfo = JSON.parse(Cookies.load('userInfo'));
        userProfile = JSON.parse(userInfo);
        resolve(userProfile);
      } else {
        Authentication.isAuthenticated().then(authenticated => {
          if (authenticated && authenticated == "true") {
            Authentication.getUserDetails().then(res => {
              if (res) {
                userProfile = res;
                resolve(userProfile);
              } else {
                reject('Not Authenticated');
              }
            });
          } else {
            this.clearSession();
            resolve(null);
          }
        });
      }
    });
  }

  static getUserProfileSM(state) {
    let userProfile = null;

    if (state.userProfile) {
      userProfile = state.userProfile;
    } else if (typeof Cookies.load('username') !== 'undefined' ||  Cookies.load('username') != null) {
      let userInfo = JSON.parse(Cookies.load('userInfo'));
      userProfile = JSON.parse(userInfo);
    }

    return userProfile;
  }

  static isAuthenticatedSM() {
    let thdsso = Cookies.load('SMSESSION');

    if(typeof thdsso === 'undefined' ||  thdsso === 'LOGGEDOFF'){
      this.clearSession();
      return Promise.resolve(false);
    } else return Promise.resolve(true);
  }


  static getReleaseNotes = () => {

    return fetch(CONFIG.authUrl + '/getReleaseNotes').then((response) => response.json()).then((responseJson) => {
      return responseJson;
    }).catch((error) => {
        return error;
      });
  };

}
