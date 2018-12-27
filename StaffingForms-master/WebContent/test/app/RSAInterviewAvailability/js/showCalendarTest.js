var Constants = new constants();
var showCalendar = new showCalendar();
fixture = document.getElementById('qunit-fixture');

QUnit.module("showCalendar", {

	setup : function(assert) {

	},

	teardown : function(assert) {

	}
});
QUnit.test("initialize", function(assert){

    showCalendar.initialize();

    assert.equal(actual, expected,'initialize successful.');
});
