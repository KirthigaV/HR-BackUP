var RSASERVICES = new rsaServices();
fixture = document.getElementById('qunit-fixture');

QUnit.module("rsaServicesTest", {

  setup : function(assert) {

  },

  teardown : function(assert) {

  }
});



QUnit.test("rsaServicesTest", function(assert){
  assert.ok(rsaServices(), '');
});