var tabNav = new tabNavigator();
var hiringEvent = new hiringEventTab();
var req = new requisitionTab();
var calendarTab = new calendarTab();
QUnit.module("tabNavigator", {
    setup: function (assert) {
        
    },
    teardown: function (assert) {
    	
    }
});

//Test case for initialize
QUnit.test("initialize", function(assert){
    tabNav.initialize();
    var actual = $("#tabMessageBarLabel").val();
    assert.equal(actual, "Select a calendar below...",'Initialize completed');
});

//Test case for hiringEventTabClick
QUnit.test("hiringEventTabClick", function(assert){
    tabNav.hiringEventTabClick();
    var actual = $("#tabMessageBarLabel").html();
    assert.equal(actual, "Select a Hiring Event below...", "hiringEventTabClick successful");
});

//Test case for requisitionTabClick
QUnit.test("requisitionTabClick", function(assert){
    tabNav.requisitionTabClick();
    var actual = $("#tabMessageBarLabel").val();
    assert.equal(actual, "Active Requisition for your Location..", "requisitionTabClick successful");
});


//Test case for calendarTabClick
QUnit.test("calendarTabClick", function(assert){
    tabNav.calendarTabClick();
    var actual = $("#tabMessageBarLabel").val();
    assert.equal(actual,  "Select a calendar below...", "calendarTabClick successful");
});
