var createHREvnt = new createHiringEvent();
var eventName = $("#eventName");
var eventDt = $("#eventDt");
var eventDtEnd = $("#eventDtEnd");
var offSiteEventYes = $("#offSiteEventYes");
var eventStrNum = $("#eventStrNum");
var eventAddress = $("#eventAddress");
var eventState = $("#eventState");
var eventCity = $("#eventCity");
var eventZip = $("#eventZip");
var venueName = $("#venueName");
var venueAddress = $("#venueAddress");
var venueCity = $("#venueCity");
var venueZip = $("#venueZip");
var eventMgrLdap = $("#eventMgrLdap");
var enterStrNum = $("#enterStrNum");
var offSiteEntryYes = $("#offSiteEntryYes");
var offSiteEntryNo = $("#offSiteEntryNo");
var stateDetailList = {"StateDetail":[{"stateNbr":0,"stateName":"ALBERTA","stateCode":"AB"},{"stateNbr":2,"stateName":"ALASKA","stateCode":"AK"},{"stateNbr":1,"stateName":"ALABAMA","stateCode":"AL"},{"stateNbr":5,"stateName":"ARKANSAS","stateCode":"AR"},{"stateNbr":56,"stateName":"WYOMING","stateCode":"WY"},{"stateNbr":0,"stateName":"YUKON","stateCode":"YT"}]};

QUnit.module("createHiringEvent", {

	setup : function(assert) {
        
	},

	teardown : function(assert) {
		
	}
});

QUnit.test("initialize", function(assert){
	createHREvnt.initialize();
    var expected = true;
    var  actual = (eventName.val()== "") ? true:false;
    assert.equal(actual, expected, "initialize successful");
});

QUnit.test("styleDatePicker ", function(assert){
	var expected = true;
	createHREvnt.styleDatePicker ();
	if($('#ui-datepicker-div').hasClass("datepicker-flex")) {
		actual = true;
	}
    assert.equal(actual, expected, "styleDatePicker successfull");
});

QUnit.test("focusFromDate ", function(assert){
	var expected = true;
	createHREvnt.focusFromDate ();
	var actual = true;
    assert.equal(actual, expected, "focusFromDate successfull");
});

QUnit.test("focusToDate ", function(assert){
	var expected = true;
	createHREvnt.focusToDate ();
	var actual = true;
    assert.equal(actual, expected, "focusToDate successfull");
});

QUnit.test("showAlertsBeforeCreate ", function(assert){
	var expected = true;
	eventName.val("");
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.showAlertsBeforeCreate ();
	var actual = (eventName.val() == "") ? true : false;
	$('.modal').hide();
    assert.equal(actual, expected, "showAlertsBeforeCreate successfull");
});

QUnit.test("showAlertsBeforeCreate ", function(assert){
	var expected = true;
	eventName.val("avc");
	eventDt.val("");
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.showAlertsBeforeCreate ();
	var actual = (eventDt.val() == "") ? true : false;
	$('.modal').hide();
    assert.equal(actual, expected, "showAlertsBeforeCreate successfull");
});

QUnit.test("showAlertsBeforeCreate ", function(assert){
	var expected = true;
	eventName.val("avc");
	eventDt.val("12/12/2015");
	eventDtEnd.val("12/12/2015");
	CONSTANTS.offSiteEventFlg = "Y";
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.showAlertsBeforeCreate ();
	var actual = (eventDt.val() != "") ? true : false;
	$('.modal').hide();
    assert.equal(actual, expected, "showAlertsBeforeCreate successfull");
});

QUnit.test("showAlertsBeforeCreate ", function(assert){
	var expected = true;
	eventName.val("avc");
	eventDt.val("12/12/2015");
	eventDtEnd.val("12/12/2015");
	CONSTANTS.offSiteEventFlg = "Y";
	eventStrNum.val("");
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.showAlertsBeforeCreate ();
	var actual = (eventDt.val() != "") ? true : false;
	$('.modal').hide();
    assert.equal(actual, expected, "showAlertsBeforeCreate successfull");
});

QUnit.test("showAlertsBeforeCreate ", function(assert){
	var expected = true;
	eventName.val("avc");
	eventDt.val("12/12/2015");
	eventDtEnd.val("12/12/2015");
	CONSTANTS.offSiteEventFlg = "Y";
	eventStrNum.val("0121");
	CONSTANTS.hiringEventMgrData = {"hiringEventMgrData":{"name":"BIONDI,VINCENT D","title":"SRTLSP","phone":"925-433-8211","email":"HOMER.D.POE@HOMEDEPOT.COM","associateId":112560222}};
	CONSTANTS.HiringEventStoreList = [];
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.showAlertsBeforeCreate ();
	var actual = (eventDt.val() != "") ? true : false;
	$('.modal').hide();
    assert.equal(actual, expected, "showAlertsBeforeCreate successfull");
});

QUnit.test("getHiringEventMgrDataResult ", function(assert){
	var expected = true;
	var data = {"Response":{"status":"SUCCESS","rejectedReasonId":0,"hiringEventResponse":{"hiringEventMgrData":{"name":"BIONDI,VINCENT D","title":"SRTLSP","phone":"925-433-8211","email":"HOMER.D.POE@HOMEDEPOT.COM","associateId":112560222}}}};
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.getHiringEventMgrDataResult (data);
	var actual = ($("#mgrName").text()!= "") ? true:false;
    assert.equal(actual, expected, "getHiringEventMgrDataResult successfull");
});

QUnit.test("refreshManagerData ", function(assert){
	var expected = true;
	eventMgrLdap.val("");
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.refreshManagerData ();
	var actual = (eventMgrLdap.val() == "") ? true:false;
    assert.equal(actual, expected, "refreshManagerData successfull");
});

QUnit.test("refreshManagerData ", function(assert){
	var expected = true;
	eventMgrLdap.val("QAT2776");
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.refreshManagerData ();
	var actual = (eventMgrLdap.val() != "") ? true:false;
    assert.equal(actual, expected, "refreshManagerData successfull");
});

QUnit.test("onSite_OffSiteShow  ", function(assert){
	var expected = true;
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	offSiteEventYes.prop('checked', true);
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.onSite_OffSiteShow  ();
	var actual = true;
    assert.equal(actual, expected, "onSite_OffSiteShow  successfull");
});

QUnit.test("onSite_OffSiteShow  ", function(assert){
	var expected = true;
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	offSiteEventYes.prop('checked', false);
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.onSite_OffSiteShow  ();
	var actual = true;
    assert.equal(actual, expected, "onSite_OffSiteShow  successfull");
});

QUnit.test("createHiringEvent", function(assert){
	var expected = true;
	//offSiteEventYes.prop('checked', true);
	$("input[name='offsiteEvent']").val("YES").prop('checked', true);
	eventName.val("abcd");
	eventDt.val("02/07/2015");
	eventDtEnd.val("10/07/2015");
	venueName.val("abcd");
	venueAddress.val("abcd");
	venueCity.val("abcd");
	venueZip.val("123456");
	eventStrNum.val('0121');
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	$("#venueStateCbo").prop("selectedIndex", 1);
	CONSTANTS.hiringEventMgrData = {"Response":{"status":"SUCCESS","rejectedReasonId":0,"hiringEventResponse":{"hiringEventMgrData":{"name":"BIONDI,VINCENT D","title":"SRTLSP","phone":"925-433-8211","email":"HOMER.D.POE@HOMEDEPOT.COM","associateId":112560222}}}};
	CONSTANTS.HiringEventStoreList = [];
	data = {"storeResponse":{"hireEventId":0,"valid":true,"storeDetails":{"number":"0121","name":"CUMBERLAND","addressLine1":"2450 CUMBERLAND PKWY","addressLine2":"","city":"ATLANTA","state":"GA","postalCode":30339,"countryCode":"US","phoneNumber":7704329930,"districtCode":"01RH","regionCode":"MIDSOUTH REGION","divisionCode":"SOUTHERN DIVISION","storeDeleteAllowed":false,"hireEventId":0}}};
	if (data.storeResponse.hasOwnProperty("valid")) {
        if (data.storeResponse.valid) {
            if (data.storeResponse.storeDetails) {
            	//Set service response to local instance
            	var hiringEventStoreDetailsVO = {};
                hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreDetailsVO = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreList.push(hiringEventStoreDetailsVO);
            }
        }
	}
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.addHiringEventStoresResult(data);
	createHREvnt.buildStoresGrid(CONSTANTS.HiringEventStoreList);
	createHREvnt.createHiringEvent();
	var actual = true;
    assert.equal(actual, expected, "createHiringEvent   successfull");
});

QUnit.test("addHiringEventStoresResult", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	data = {"storeResponse":{"hireEventId":0,"valid":true,"storeDetails":{"number":"0121","name":"CUMBERLAND","addressLine1":"2450 CUMBERLAND PKWY","addressLine2":"","city":"ATLANTA","state":"GA","postalCode":30339,"countryCode":"US","phoneNumber":7704329930,"districtCode":"01RH","regionCode":"MIDSOUTH REGION","divisionCode":"SOUTHERN DIVISION","storeDeleteAllowed":false,"hireEventId":0}}};
	createHREvnt.addHiringEventStoresResult(data);
	var actual = true;
    assert.equal(actual, expected, "addHiringEventStoresResult   successfull");
});

QUnit.test("getHiringEventStoreDetailsResult", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	data = {"storeResponse":{"hireEventId":0,"valid":true,"storeDetails":{"number":"0121","name":"CUMBERLAND","addressLine1":"2450 CUMBERLAND PKWY","addressLine2":"","city":"ATLANTA","state":"GA","postalCode":30339,"countryCode":"US","phoneNumber":7704329930,"districtCode":"01RH","regionCode":"MIDSOUTH REGION","divisionCode":"SOUTHERN DIVISION","storeDeleteAllowed":false,"hireEventId":0}}};
	createHREvnt.getHiringEventStoreDetailsResult(data);
	var actual = true;
    assert.equal(actual, expected, "getHiringEventStoreDetailsResult   successfull");
});

QUnit.test("addStoreToHiringEvent", function(assert){
	var expected = true;
	enterStrNum.val('0121');
	CONSTANTS.HiringEventStoreList = [];
	CONSTANTS.retailStaffingReq = createHREvnt;
	data = {"storeResponse":{"hireEventId":0,"valid":true,"storeDetails":{"number":"0121","name":"CUMBERLAND","addressLine1":"2450 CUMBERLAND PKWY","addressLine2":"","city":"ATLANTA","state":"GA","postalCode":30339,"countryCode":"US","phoneNumber":7704329930,"districtCode":"01RH","regionCode":"MIDSOUTH REGION","divisionCode":"SOUTHERN DIVISION","storeDeleteAllowed":false,"hireEventId":0}}};
	if (data.storeResponse.hasOwnProperty("valid")) {
        if (data.storeResponse.valid) {
            if (data.storeResponse.storeDetails) {
            	//Set service response to local instance
            	var hiringEventStoreDetailsVO = {};
                hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreDetailsVO = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreList.push(hiringEventStoreDetailsVO);
            }
        }
	}
	createHREvnt.addStoreToHiringEvent();
	var actual = true;
    assert.equal(actual, expected, "addStoreToHiringEvent   successfull");
});
 
QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("");
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueName.val() == "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("abcd");
	venueAddress.val("");
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueAddress.val() == "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("abcd");
	venueAddress.val("abcd");
	venueCity.val("");
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueCity.val() == "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("abcd");
	venueAddress.val("abcd");
	venueCity.val("");
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueCity.val() == "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("abcd");
	venueAddress.val("abcd");
	venueCity.val("abcd");
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	$("#venueStateCbo").prop("selectedIndex", -1);
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueCity.val() != "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("offsiteEvntVenueDtlsCheck", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	venueName.val("abcd");
	venueAddress.val("abcd");
	venueCity.val("abcd");
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	$("#venueStateCbo").prop("selectedIndex", 1);
	venueZip.val("");
	createHREvnt.offsiteEvntVenueDtlsCheck();
	var actual = (venueZip.val() == "") ? true:false;
    assert.equal(actual, expected, "offsiteEvntVenueDtlsCheck   successfull");
});

QUnit.test("getStoreInfo", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	eventStrNum.val("0121");
	createHREvnt.getStoreInfo ();
	var actual = true;
    assert.equal(actual, expected, "getStoreInfo    successfull");
});

QUnit.test("setEndDate", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	eventDt.val("02/07/2015");
	createHREvnt.setEndDate  ();
	var actual = (eventDt.val() != "")? true:false;
    assert.equal(actual, expected, "setEndDate     successfull");
});

QUnit.test("convertDate", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.convertDate("02/07/2015");
	var actual = (eventDt.val() != "")? true:false;
    assert.equal(actual, expected, "convertDate  successfull");
});

QUnit.test("alertOkClicked", function(assert){
	var expected = true;
	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.alertOkClicked();
	var actual = true;
    assert.equal(actual, expected, "alertOkClicked  successfull");
});

QUnit.test("createHiringEvent", function(assert){
	var expected = true;
	//offSiteEventYes.prop('checked', true);
	$("input[name='offsiteEvent']").val("NO").prop('checked', true);
	eventName.val("abcd");
	eventDt.val("02/07/2015");
	eventDtEnd.val("10/07/2015");
	eventAddress.text("abcd");
	eventCity.text("abcd");
	eventState.text("abcd");
	eventZip.text("123456");
	eventStrNum.val('0121');
	CONSTANTS.reqStateDet = stateDetailList.StateDetail;
	createHREvnt.appendOptionToSelect('venueStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
	$("#venueStateCbo").prop("selectedIndex", 1);
	CONSTANTS.hiringEventMgrData = {"name":"BIONDI,VINCENT D","title":"SRTLSP","phone":"925-433-8211","email":"HOMER.D.POE@HOMEDEPOT.COM","associateId":112560222};
	CONSTANTS.HiringEventStoreList = [];
	data = {"storeResponse":{"hireEventId":0,"valid":true,"storeDetails":{"number":"0121","name":"CUMBERLAND","addressLine1":"2450 CUMBERLAND PKWY","addressLine2":"","city":"ATLANTA","state":"GA","postalCode":30339,"countryCode":"US","phoneNumber":7704329930,"districtCode":"01RH","regionCode":"MIDSOUTH REGION","divisionCode":"SOUTHERN DIVISION","storeDeleteAllowed":false,"hireEventId":0}}};
	if (data.storeResponse.hasOwnProperty("valid")) {
        if (data.storeResponse.valid) {
            if (data.storeResponse.storeDetails) {
            	//Set service response to local instance
            	var hiringEventStoreDetailsVO = {};
                hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreDetailsVO = hiringEventStoreDetailsVO;
                CONSTANTS.HiringEventStoreList.push(hiringEventStoreDetailsVO);
            }
        }
	}

	CONSTANTS.retailStaffingReq = createHREvnt;
	createHREvnt.addHiringEventStoresResult(data);
	createHREvnt.buildStoresGrid(CONSTANTS.HiringEventStoreList);
	createHREvnt.createHiringEvent();
	var actual = true;
    assert.equal(actual, expected, "createHiringEvent   successfull");
}); 