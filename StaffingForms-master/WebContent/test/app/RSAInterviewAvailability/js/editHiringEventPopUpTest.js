var CONSTANTS = new constants();
var editHiringEventObj =  new editHiringEvent();
fixture = document.getElementById('qunit-fixture');

QUnit.module("hiringCalendarTest", {

    setup : function(assert) {

    },

    teardown : function(assert) {

    }
});


QUnit.test("initialize", function(assert){
    editHiringEventObj.initialize();
    assert.equal(true, false,'initialize successful.');
});


QUnit.test("setDirtyPage", function(assert){
    editHiringEventObj.setDirtyPage();
    assert.equal(true, false,'setDirtyPage successful.');
});

QUnit.test("addValue", function(assert){
    editHiringEventObj.addValue();
    assert.equal(true, false,'addValue successful.');
});


QUnit.test("focusFromDate", function(assert){
    editHiringEventObj.focusFromDate();
    assert.equal(true, false,'focusFromDate successful.');
});

QUnit.test("focusToDate", function(assert){
    editHiringEventObj.focusToDate();
    assert.equal(true, false,'focusToDate successful.');
});

QUnit.test("addStoreToHiringEvent", function(assert){
    editHiringEventObj.addStoreToHiringEvent();
    assert.equal(true, false,'addStoreToHiringEvent successful.');
});


QUnit.test("onSite_OffSiteShow", function(assert){
    editHiringEventObj.onSite_OffSiteShow(false);
    assert.equal(true, false,'onSite_OffSiteShow successful.');
});


QUnit.test("setLocationValue", function(assert){
    editHiringEventObj.setLocationValue(null, null, null);
    assert.equal(true, false,'setLocationValue successful.');
});

QUnit.test("setParticipatingStores", function(assert){
    editHiringEventObj.setParticipatingStores(false);
    assert.equal(true, false,'setParticipatingStores successful.');
});


QUnit.test("setEventType", function(assert){
var expected = true;
var tempArray = [];
var result = editHiringEventObj.setEventType(tempArray);
var actual = ("OSE"==result || "SSE"==result);
assert.equal(actual, expected,'setEventType successful.');
});


QUnit.test("validateUpdateHiringEvent", function(assert){
var expected = true;
var actual = editHiringEventObj.validateUpdateHiringEvent();
assert.equal(actual, expected,'validateUpdateHiringEvent successful.');
});