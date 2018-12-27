var createHiringEventPopup = new createHiringEvent();
fixture = document.getElementById('qunit-fixture');
QUnit.module("CalendarTest", {
    setup: function (assert) {
        
    },
    teardown: function (assert) {
        
    }
});

//Test case for initialize
QUnit.test("initialize", function(assert){
    createHiringEventPopup.initialize();
    var actual = $("#tabMessageBarLabel").val();
    assert.equal(actual, "Select a calendar below...",'Initialize completed');
});



QUnit.test("onSite_OffSiteShow", function(assert){
var columns = createHiringEventPopup.onSite_OffSiteShow();
var expected = false;
var actual = (columns==undefined);
assert.equal(actual, expected,'onSite_OffSiteShow successful.');
});


QUnit.test("addStoreToHiringEvent", function(assert){
var columns = createHiringEventPopup.addStoreToHiringEvent();
var expected = false;
var actual = (columns==undefined);
assert.equal(actual, expected,'addStoreToHiringEvent successful.');
});


QUnit.test("checkHiringEventStoreList", function(assert){
var expected = true;
var actual = createHiringEventPopup.checkHiringEventStoreList();
assert.equal(actual, expected,'checkHiringEventStoreList successful.');
});


QUnit.test("validateCreateHiringEvent", function(assert){
var expected = true;
var actual = createHiringEventPopup.validateCreateHiringEvent();
assert.equal(actual, expected,'validateCreateHiringEvent successful.');
});


QUnit.test("sethiringEventDetailsVO", function(assert){
var expected = true;
var actual = createHiringEventPopup.sethiringEventDetailsVO();
assert.equal(actual, expected,'sethiringEventDetailsVO successful.');
});

QUnit.test("setTempStoreList", function(assert){
var expected = true;
var actual = (undefined==createHiringEventPopup.setTempStoreList());
assert.equal(actual, expected,'setTempStoreList successful.');
});


QUnit.test("setEventType", function(assert){
var expected = true;
var tempArray = [];
var result = createHiringEventPopup.setEventType(tempArray);
var actual = ("OSE"==result || "SSE"==result);
assert.equal(actual, expected,'setEventType successful.');
});

