/*jslint node: true */
"use strict";

let express = require('express');
let request = require('request');
let xmlToJson = require('xml2js').parseString;
let path = require('path');
let router = express.Router();


module.exports = function (server, config) {

  let NEW_RELEASE, NEXT_RELEASE;

  if(process.env.VCAP_SERVICES){
    let vcap_services = JSON.parse(process.env.VCAP_SERVICES);
    NEW_RELEASE = vcap_services['user-provided'][0].credentials.new_release;
    NEXT_RELEASE = vcap_services['user-provided'][0].credentials.next_release;
  } else {
    NEW_RELEASE = "New Release";
    NEXT_RELEASE = "Next Release";
  }

// Step 1: Function which is called when asking the user to login
  router.post('/ssoLogin', function (req, res) {
    var url = config.ssoUrl + '/thdLogin';
    var data = req.body;

    console.log('Inside SSO Login : ');

    data.j_password = new Buffer(data.j_password, 'base64').toString('utf8');
    request.post(url, function (error, response, body) {
      var jsonBody, jsonRaw;
      xmlToJson(body, {explicitArray: false, ignoreAttrs: true}, function (err, jsonResult) {
        jsonRaw = jsonResult;
        jsonBody = JSON.stringify(jsonResult);
      });
      res.statusCode = response.statusCode;
      res.statusMessage = response.statusMessage;
      if (response.statusCode === 200) {
        res.setHeader('Set-Cookie', response.headers['set-cookie']);
      } else {
        jsonBody = JSON.stringify(jsonRaw.THDLogin.Error);
      }
      res.end(jsonBody);
    }).form(data);
  });


  router.get('/isSessionValid', function (req, res) {
    var url = config.ssoUrl + '/isSessionValid?callingProgram=' + config.projectInfo.appName;
    var options = {
      url: url,
      headers: {
        'Cookie': req.headers['cookie']
      }
    };

    request.get(options, function (error, response, body) {
      var jsonBody, jsonRaw;
      xmlToJson(body, {explicitArray: false, ignoreAttrs: true}, function (err, jsonResult) {
        jsonRaw = jsonResult;
        jsonBody = JSON.stringify(jsonResult);
      });
      res.statusCode = response.statusCode;
      res.statusMessage = response.statusMessage;

      if (response.statusCode === 200) {
        jsonBody = JSON.stringify(jsonRaw.IsSessionValid);
        console.log(jsonBody);
      }
      res.end(jsonBody);
    });
  });


  router.get('/getUserInfo', (req, res) => {

    let cookieVal = JSON.stringify(req.headers);
    cookieVal = cookieVal.split('; ');
    let result = {};
    for (let i = 0; i < cookieVal.length; i++) {
      let arr = cookieVal[i].split('=');
      result[arr[0]] = arr[1];
    }

    request.get({
      url: config.apiurl + 'userInfo/' + req.query.username,
      headers: {
        "Authorization": result['THDSSO'],
        "api_key": config.apikey
      }
    }, function (error, response, body) {
      if (error) {
        console.log(error);
        res.send(error.toJSON());
      } else {
        xmlToJson(body, {explicitArray: false, ignoreAttrs: true}, function () {
          console.log('User Info response body : ' + body);
          let cookieExpiryTime = new Date(Date.now() + (12 * 60 * 60 * 1000));
          console.log('cookie expiry time : ' + cookieExpiryTime);
          const userInfo = body.toString();
          console.log("UserInfo-----" + userInfo);
          res.cookie('userInfo', JSON.stringify(body), {expires: cookieExpiryTime});
          res.cookie('username', req.query.username, {expires: cookieExpiryTime});
          res.end(body);
        });
      }
    });
  });

  router.get('/getUserProfile', (req, res) => {
    request.get({
      url: config.ssoUrl + '/getUserProfile?callingProgram=' + config.projectInfo.appName,
      headers: {
        'Cookie': req.headers['cookie']
      }
    }, function (error, response, body) {
      if (error) {
        console.log(error);
        res.send(error.toJSON());
      } else {
        xmlToJson(body, {explicitArray: false, ignoreAttrs: true}, function (error, jsonResult) {
          let cookieExpiryTime = new Date(Date.now() + (12 * 60 * 60 * 1000));
          res.cookie('username', jsonResult.GetUserProfile.UserProfile.UserID, {expires: cookieExpiryTime});
          res.redirect('/getUserInfo?username=' + jsonResult.GetUserProfile.UserProfile.UserID);
          res.end(body);
        });
      }
    });
  });

  router.get('/getReleaseNotes', (req, res) => {
    var obj = {
      "newRelease": NEW_RELEASE,
      "nextRelease": NEXT_RELEASE
    };
    res.send(obj);
  });


  // The following allows for Node JS server in QA and Prod to sync its web server with client routes. This is not seen
  // when running locally as that uses the webpack server instead of node JS server.

  // Notice, using a catch all will sync all pages; however, for a page that does not exist on route, will go to blank index page on server refresh.
  // Therefore, not using a catch all * and adding all client routes here, will allow a page that does not exist to go to home by default and routes to be in sync


  router.get('/home', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  router.get('/login', (req, res) => {
    router.use(express.static(path.resolve(__dirname, '..', 'dist')));
    res.sendFile(path.resolve(__dirname, '..', 'dist', 'index.html'));
  });

  router.get('/logout', (req, res) => {
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
