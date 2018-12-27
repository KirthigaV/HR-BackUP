/*jslint node: true */
'use strict';

var express = require('express');
var bodyParser = require('body-parser');
var compression = require('compression');
var cors = require('cors');
var enforceSsl = require('express-sslify');
var request = require('request');
var sts = require('strict-transport-security');

var config;
if (process.env.config) {
  config = JSON.parse(process.env.config);
} else {
  var configJson = require('./config/config.json');
  config = configJson.local;
}

var app = express();

app.enable('trust proxy');
if (process.env.NODE_ENV !== "local") {
  app.use(sts.getSTS({ "max-age": { days: 90 } }));
  app.use(enforceSsl.HTTPS());
  app.use(cors({ credentials: true, origin: true }));
}

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(compression());
app.use('/assets', express.static(__dirname + '/dist/assets/', { redirect: false }));
app.use('/', express.static(__dirname + '/dist/', { redirect: false }));

app.use(function (req, res, next) {
  var origin = req.headers.origin;
  if (origin && origin.includes('.homedepot.com')) {
    res.header('Access-Control-Allow-Origin', origin);
    res.header('Access-Control-Allow-Credentials', true);
    res.header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  }
  next();
});

/* Express Services */
var security = require('./server/security.js');
security(app, config);

app.get('/config', function (req, res) {
  res.json(config);
});

app.all('*', function (req, res) {
  res.redirect('/');
});

var port = process.env.PORT || 3000;

var webServer = app.listen(port, function () {
  console.log('Listening on port %d', webServer.address().port);
});
