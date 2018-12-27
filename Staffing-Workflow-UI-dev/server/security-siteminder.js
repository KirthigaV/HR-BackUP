/*jslint node: true */
"use strict";

let express = require('express');
let request = require('request');
const path = require('path');
let router = express.Router();

module.exports = function (server, config) {

  let CLIENT_ID, CLIENT_SECRET, AUTH_DOMAIN, NEW_RELEASE, NEXT_RELEASE;
  let protocol = 'http';

  if(process.env.VCAP_SERVICES){
    protocol = 'https';
    let vcap_services = JSON.parse(process.env.VCAP_SERVICES);
    CLIENT_ID = vcap_services['user-provided'][0].credentials.client_id;
    CLIENT_SECRET = vcap_services['user-provided'][0].credentials.client_secret;
    AUTH_DOMAIN = vcap_services['user-provided'][0].credentials.auth_domain;
    NEW_RELEASE = vcap_services['user-provided'][0].credentials.new_release;
    NEXT_RELEASE = vcap_services['user-provided'][0].credentials.next_release;
    console.log('NEW_RELEASE : '+NEW_RELEASE);
    console.log('NEXT_RELEASE : '+NEXT_RELEASE);
  } else {
    CLIENT_ID = "0e235046-9e37-475a-a288-5ab549b48ad5";//process.env.CLIENT_ID;
    CLIENT_SECRET = "9e440807-c431-4fe1-84b8-c4a64204e4ad";//process.env.CLIENT_SECRET;
    AUTH_DOMAIN = "https://auth.login.run-np.homedepot.com";//process.env.AUTH_DOMAIN;
    NEW_RELEASE = "New Release Value|Another new release|Third New Release|Fourth Release";
    NEXT_RELEASE = "Next Release string|Another next release|Third next Release|Fourth Next Release";
    console.log('NEW_RELEASE ELSE: '+NEW_RELEASE);
    console.log('NEXT_RELEASE ELSE: '+NEXT_RELEASE);
  }
  console.log('sso protocol: ' + protocol);
  console.log('sso CLIENT_ID: ' + CLIENT_ID);
  console.log('sso AUTH_DOMAIN: ' + AUTH_DOMAIN);

  let app_url;
  const CALLBACK_PATH = '/callback';
  let req_url;

// Step 1: Function which is called when asking the user to login
  router.get('/ssoLogin', function (req, res) {
    let host = req.get('host');
    console.log('sso host: ' + host);
    console.log('sso path: ' + req.path);
    console.log('sso query: ' + req.url);
    app_url = protocol + '://' + host;
    req_url = req.url;
    let encodedUri = encodeURIComponent(app_url + CALLBACK_PATH);
    let redirectUrl = config.ssoUrl  + '/oauth/authorize' + '?response_type=code&client_id=' + CLIENT_ID + '&redirect_uri=' + encodedUri;
    console.log('Redirecting to auth url: ' + redirectUrl);
    res.redirect(redirectUrl);
  });

  router.get('/getReleaseNotes', (req, res) => {
    var obj = {
      "newRelease": NEW_RELEASE,
      "nextRelease": NEXT_RELEASE
    };
    res.send(obj);
  });

  router.get('/callback', function (req, res) {

    console.log('SSO callback called, code: ' + req.query.code);
    let authTokenUrl = config.ssoUrl  + '/oauth/token';
    //console.log(authTokenUrl);
    request({
      url : authTokenUrl,
      method : "POST",
      form : {
        "client_id" : CLIENT_ID,
        "code" : req.query.code,
        "grant_type" : "authorization_code",
        "redirect_uri" : app_url + CALLBACK_PATH,
        "client_secret" : CLIENT_SECRET
      }
    },function(err,result,body){
      if (err) {
        console.log('Error when trying to get auth token: ' + err);
        return;
      }
      //console.log('Auth token response: ' + body);

      let access_token = JSON.parse(body).access_token;
      //console.log('access_token: ' + access_token);

      //Now we are logged in. Use the auth token to do what we want. E.g. get userinfo from eht oauth server
      getUserLDAP(res, access_token);
    });
  });

// Step 3: Function to get user info using a token
  function getUserLDAP(res, access_token){
    let userLDAPUrl = config.ssoUrl  + '/userinfo';
    request({
      url : userLDAPUrl,
      method : "GET",
      headers: {
        'Authorization': 'Bearer ' + access_token
      }
    },function(err,result,body){
      if (err) {
        console.log('Error when trying to get user LDAP: ' + err);
        return;
      }


      //Set SSO cookeis with expiry time equal to 12 hours
      let cookieExpiryTime = new Date(Date.now() + (12*60*60*1000));
      console.log('cookie expiry time: ' + cookieExpiryTime);
      console.log('User Info response: ' + body);
      let obj = JSON.parse(body);
      console.log("-----"+obj.user_name);
      let ldap = obj.user_name;
      res.cookie('username', ldap, {expires: cookieExpiryTime});
      res.cookie('access_token', access_token, {expires: cookieExpiryTime});
      getUserInfo(res,ldap,access_token);
    });
  }

  function getUserInfo(res,ldap,access_token) {
    let userInfoUrl = config.apiurl+'userInfo/'+ldap;

    request({
      url : userInfoUrl,
      method : "GET",
      headers: {
        "Content-type":"application/json",
        'Authorization': access_token
      }
    },function(err,result,body){
      if (err) {
        console.log('Error when trying to get user info: ' + err);
        return;
      }

      //Set SSO cookeis with expiry time equal to 12 hours
      let cookieExpiryTime = new Date(Date.now() + (12*60*60*1000));
      console.log('cookie expiry time: ' + cookieExpiryTime);
      console.log('User Info response: ' + body);
      let obj = JSON.parse(body);
      const userInfo = body.toString();
      console.log("UserInfo-----"+userInfo);
      res.cookie('userInfo',JSON.stringify(body), {expires: cookieExpiryTime});
      res.cookie('userInfo1',obj, {expires: cookieExpiryTime});
      let redirectURL = '/home';
      if(!process.env.VCAP_SERVICES){
        redirectURL = 'http://localhost.homedepot.com:3001/home';
      }
      console.log("----redirectURL----"+redirectURL);
      res.redirect(redirectURL);
    });

  }

  router.get('/getReleaseNotes', (req, res) => {
    var obj = {
      "NEW_RELEASE": NEW_RELEASE,
      "NEXT_RELEASE": NEXT_RELEASE
    };
    res.send(obj);
  });

// Last Step: Function to logout, just redirect user to logout url
  router.get('/logout', function (req, res) {
    console.log('Logout user...');
    const logoutUrl = config.ssoUrl + '/logout.do?CLIENT_ID=' + CLIENT_ID;
    res.redirect(logoutUrl);
  });

  router.get('/home', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  router.get('/requisitions', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  router.get('/requisitions/:reqId/:reqType', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  router.get('/intCalendarDetails', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  server.use(router);

};

