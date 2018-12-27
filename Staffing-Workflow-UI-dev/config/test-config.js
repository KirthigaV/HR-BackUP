var AppConfig = (function () {
    var jsonResponse = readJSON('config/config.json');
    var config = jsonResponse.local;
    return config;
})();

var AppVersion = 1;
