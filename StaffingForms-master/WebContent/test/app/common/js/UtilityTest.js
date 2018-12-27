fixture = document.getElementById('qunit-fixture');

QUnit.module("Utility", {

	setup : function(assert) {
		
	},

	teardown : function(assert) {
		
	}
});


QUnit.test("formattedDateValid", function(assert){
    var expected =  "2015-6-17";
    var actual = UTILITY.formattedDate(new Date(2015, 05, 17, 11, 39, 52, 000));	
    assert.equal(actual, expected,'formattedDateValid successful.');
});


QUnit.test("formatedMonthValid", function(assert){
    var expected =  "2015-06-17";
    var actual = UTILITY.formatedMonth(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'formatedMonthValid successful.');
});


QUnit.test("stringFormattedDate", function(assert){
    var expected =  "2015/6/17";
    var actual = UTILITY.stringFormattedDate(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'stringFormattedDate successful.');
});
QUnit.test("getMonthFormatted", function(assert){
    var expected =  "Wed Jun 17 2015";
    var actual = UTILITY.getMonthFormatted(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'getMonthFormatted successful.');
});
QUnit.test("formatTimeStamp", function(assert){
    var expected =  "2015-6-17 11:39:52.0";
    var actual = UTILITY.formatTimeStamp(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'formatTimeStamp successful.');
});

QUnit.test("addThirtyMinutesToStamp", function(assert){
    var expected =  "2015-6-17 12:09:52.0";
    var actual = UTILITY.addThirtyMinutesToStamp(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'addThirtyMinutesToStamp successful.');
});
QUnit.test("formatTimeStamp", function(assert){
    var expected =  "2015-6-17 11:39:52.0";
    var actual = UTILITY.formatTimeStamp(new Date(2015, 05, 17,11, 39, 52, 000));	
    assert.equal(actual, expected,'formatTimeStamp successful.');
});

QUnit.test("IsArray", function(assert){
    var expected =  true;
    var cars = ["Saab", "Volvo", "BMW"];
    var actual = UTILITY.IsArray(cars);	
    assert.equal(actual, expected,'IsArray successful.');
});
QUnit.test("isDateInHiringEvent", function(assert){
    var expected =  "true";
    var actual = UTILITY.isDateInHiringEvent(new Date(2015, 05, 17,11, 39, 52, 000), new Date(2015, 05, 16,11, 39, 52, 000), new Date(2015, 05, 18,11, 39, 52, 000));	
    assert.equal(actual, expected,'isDateInHiringEvent successful.');
});

