var calendarJs = new calendarTab();
var userProfileResponse = {"UserProfileResponseDTO":{"userId":"QAT2615","isValidRole":false,"firstName":"TESTER","lastName":"QATESTER","strNbr":9100,"Status":"SUCCESS","ErrorCd":0}};

QUnit.module("Calendar", {
    setup: function (assert) {
        
    },
    teardown: function (assert) {
        
    }
});

QUnit.test("setColumns", function(assert){
var columns = calendarJs.setColumns();
var expected = false;
var actual = (columns==undefined);
assert.equal(actual, expected,'setColumns successful.');
});


QUnit.test("getUserprofile", function(assert){
var  expected = 9100;
    $.ajax = function(options) {
        options.success = function(data) {
            calendarJs.getUserprofile();
        };
        options.success(userProfileResponse);
    }; 
var actual = $("#storeLocation").val();
assert.equal(actual, expected,'getUserprofile successful.');
});


QUnit.test("successUserProfile", function(assert){
var columns = calendarJs.successUserProfile(userProfileResponse);
var  expected = 9100;
var actual = $("#storeLocation").val();
assert.equal(actual, expected,'getUserprofile successful.');
});

QUnit.test("loadGrid", function(assert){
var columns = calendarJs.loadGrid();
var expected = true;
var actual = true;
assert.equal(actual, expected,'loadGrid successful.');
});


QUnit.test("validStore", function(assert){
var columns = calendarJs.validStore();
var expected = true;
var actual = true;
assert.equal(actual, expected,'validStore successful.');
});