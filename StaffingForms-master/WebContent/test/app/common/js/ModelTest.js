var  Model  = new Object();
fixture = document.getElementById('qunit-fixture');

QUnit.module("Model", {

	setup : function(assert) {

	},

	teardown : function(assert) {

	}
});



QUnit.test("Model", function(assert){
	assert.ok(Model(), '');
});