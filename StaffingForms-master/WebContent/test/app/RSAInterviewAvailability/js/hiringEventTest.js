var Constants = new constants();
var hiringEvent = new hiringEventTab();
fixture = document.getElementById('qunit-fixture');

QUnit.module("hiringEventTest", {

    setup : function(assert) {

    },

    teardown : function(assert) {

    }
});
QUnit.test("initialize", function(assert){

    hiringEvent.initialize();

    assert.equal(true, false,'initialize successful.');
});


QUnit.test("setColumns", function(assert){

    hiringEvent.setColumns();

    assert.equal(true, false,'setColumns successful.');
});


QUnit.test("loadData", function(assert){

    hiringEvent.loadData();

    assert.equal(true, false,'loadData successful.');
});


QUnit.test("loadGrid", function(assert){

    hiringEvent.loadGrid();

    assert.equal(true, false,'loadGrid successful.');
});