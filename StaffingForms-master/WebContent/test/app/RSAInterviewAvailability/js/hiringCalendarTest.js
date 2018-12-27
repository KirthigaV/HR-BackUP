var Constants = new constants();
var showHiringCalendar =  new showHiringCalendar();
fixture = document.getElementById('qunit-fixture');

QUnit.module("hiringCalendarTest", {

    setup : function(assert) {

    },

    teardown : function(assert) {

    }
});
QUnit.test("initialize", function(assert){
    showHiringCalendar.initialize();
    assert.equal(true, false,'initialize successful.');
});


QUnit.test("changeCalendarSummary", function(assert){
    showHiringCalendar.changeCalendarSummary();
    assert.equal(true, false,'changeCalendarSummary successful.');
});


QUnit.test("selectNext", function(assert){
    showHiringCalendar.selectNext();
    assert.equal(true, false,'selectNext successful.');
});

QUnit.test("selectPrevious", function(assert){
    showHiringCalendar.selectPrevious();
    assert.equal(true, false,'selectPrevious successful.');
});


QUnit.test("getCalendarSummary", function(assert){
    showHiringCalendar.getCalendarSummary(getCalendarSummaryResponse);
    assert.equal(true, false,'getCalendarSummary successful.');
});


QUnit.test("getPopup", function(assert){
    showHiringCalendar.getPopup();
    assert.equal(true, false,'getPopup successful.');
});


QUnit.test("selectDayPrevious", function(assert){
    showHiringCalendar.selectDayPrevious();
    assert.equal(true, false,'selectDayPrevious successful.');
});


QUnit.test("selectDayNext", function(assert){
    showHiringCalendar.selectDayNext();
    assert.equal(true, false,'selectDayNext successful.');
});


QUnit.test("viewTimeBlock", function(assert){
    showHiringCalendar.viewTimeBlock(viewTimeBlockresponse);
    assert.equal(true, false,'viewTimeBlock successful.');
});

QUnit.test("loadViewButtons", function(assert){
    showHiringCalendar.loadViewButtons();
    assert.equal(true, false,'loadViewButtons successful.');
});


QUnit.test("checkForScheduledInterviews", function(assert){
    showHiringCalendar.checkForScheduledInterviews();
    assert.equal(true, false,'checkForScheduledInterviews successful.');
});


QUnit.test("addTimeBlock", function(assert){
    showHiringCalendar.addTimeBlock(addTimeBlockResponse);
    assert.equal(true, false,'addTimeBlock successful.');
});

QUnit.test("addInterviewSlot", function(assert){
    showHiringCalendar.addInterviewSlot();
    assert.equal(true, false,'addInterviewSlot successful.');
});

QUnit.test("addSelectAll", function(assert){
    showHiringCalendar.addSelectAll();
    assert.equal(true, false,'addSelectAll successful.');
});


QUnit.test("loadBlockTime", function(assert){
    showHiringCalendar.loadBlockTime();
    assert.equal(true, false,'loadBlockTime successful.');
});

QUnit.test("submitAddConfirmBlock", function(assert){
    showHiringCalendar.submitAddConfirmBlock();
    assert.equal(true, false,'submitAddConfirmBlock successful.');
});


QUnit.test("confirmSlot", function(assert){
    showHiringCalendar.confirmSlot();
    assert.equal(true, false,'confirmSlot successful.');
});

QUnit.test("addWarningNotifications", function(assert){
    showHiringCalendar.addWarningNotifications();
    assert.equal(true, false,'addWarningNotifications successful.');
});

