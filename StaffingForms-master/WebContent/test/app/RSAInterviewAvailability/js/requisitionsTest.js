var requisitionsTab = new requisitionTab();

fixture = document.getElementById('qunit-fixture');

QUnit.module("requisitionsTest", {

    setup : function(assert) {

    },

    teardown : function(assert) {

    }
});
//Test case for initialize
QUnit.test("initialize", function(assert){

    requisitionsTab.initialize();

    assert.equal(actual, expected,'initialize successful.');
});



QUnit.test("getOptions", function(assert){
var options = requisitionsTab.getOptions();
var expected = false;
var actual = (options==undefined);
assert.equal(actual, expected,'getOptions successful.');
});


QUnit.test("getColumns", function(assert){
var columns = requisitionsTab.getColumns();
var expected = false;
var actual = (columns==undefined);
assert.equal(actual, expected,'getColumns successful.');
});
