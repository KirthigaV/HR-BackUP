/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: createHiringEvent.js
 * Application: RetailStaffing
 *
 * Create Hiring Event based on the request
 */
function createHiringEvent() {
    // Declare variables
    var eventName = $("#eventName");
    var eventDt = $("#eventDtStart");
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
    /*
     * Build Store details and handle UI controls
     */
    this.initialize = function() {
        // Set empty values
        $('#ui-datepicker-div').hide();
        this.popup = new rsaPopup();
        $("#createHiringEventPopUp").find('.modal-dialog').addClass("modal-dialog-center");
        // Set top and left margin to look alike flex screens
        $("#createHiringEventPopUp").find('.modal-dialog').css({
            'margin-top' : function() {
                return -($('#createHiringEventPopUp .modal-dialog').outerHeight() / 2);
            },
            'margin-left' : function() {
                return -($('#createHiringEventPopUp .modal-dialog').outerWidth() / 2);
            }
        });
        eventDt.val("");
        eventDtEnd.val("");
        eventName.val("");
        eventStrNum.val("");
        eventAddress.text("");
        eventCity.text("");
        eventZip.text("");
        eventState.text("");
        venueName.val("");
        venueAddress.val("");
        venueCity.val("");
        venueZip.val("");
        $("#offSiteEventNo").click();
        $("#venueStateCbo").empty();
        $("#mgrName").text("");
        $("#mgrTitle").text("");
        $("#mgrPhn").text("");
        $("#mgrMail").text("");
        $("#refreshMgrData").blur();
        this.startDateBeginRange = new Date();
        this.endDateBeginRange = new Date();
        CONSTANTS.retailStaffingReq = this;
        CONSTANTS.retailStaffingReq.model = {};
        $(".gridContainer").empty();
        CONSTANTS.retailStaffingReq.gridData = [];
        // Build Store grid details
        CONSTANTS.retailStaffingReq.buildStoresGrid(CONSTANTS.retailStaffingReq.gridData);

        // Clear Hiring Event Manager Data
        CONSTANTS.hiringEventMgrData = null;
        CONSTANTS.HiringEventStoreList = [];

        // Clear Hiring Event Store Details
        CONSTANTS.hiringEventStoreDetails = null;
        offSiteEntryYes.addClass('hide');
        offSiteEntryNo.removeClass('hide');

        var days = [ "S", "M", "T", "W", "T", "F", "S" ];
        // Declare datepicker - set format as mm/dd/yy
        $('#eventDtEnd').datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            minDate : 1,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top",
            dayNamesMin : days

        });
        var minDate = new Date();
        minDate.setDate(minDate.getDate() + 1);
        this.startDateBeginRange = minDate;
        var tempStartDate = this.startDateBeginRange;
        this.endDateBeginRange = minDate;
        $('#eventDtStart').datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            minDate : tempStartDate,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top",
            dayNamesMin : days

        });
        $("#eventStrNum").attr("title",CONSTANTS.CREATE_HIRING_EVENT_STORE_NUMBER_TOOLTIP);
    };

    /*
     * To style the date-picker.
     */
    this.styleDatePicker = function() {
        // Apply styles to appear like flex
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    /*
     * this keypress eventName box validation
     */
    $("#eventName").keypress(function(e) {
        var temp = String.fromCharCode(e.which);
        if (!(/[0-9A-Za-z\-\,\'\(\)\_\. ]/.test(temp))) {
            return false;
        }
    });
    /*
     * Format Option values
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray, textKey, valueKey) {
        if (action === "append") {
            // Set values
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
            }
            $('#' + drpDownId).attr('disabled', false);
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
        } else {
            // Remove values and disable control
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
        }

    };
    this.eventNameChange = function() {
        $("#refreshMgrData").blur();
    };
    /*
     * Set Focus from date
     */
    this.focusFromDate = function() {
        $('#eventDtStart').focus().select();
    };
    /*
     * Set Focus to date
     */
    this.focusToDate = function() {
        $('#eventDtEnd').focus().select();
    };

    /*
     * Adds Store to Hiring Event
     */
    this.addStoreToHiringEvent = function() {
        var storeExists = false;
        CONSTANTS.retailStaffingReq.showDefaultCursor();

        if (enterStrNum.val().length === 4) {
            // Make sure that the requested Store is not already in the list
            for ( var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
                if (enterStrNum.val().toString() === CONSTANTS.HiringEventStoreList[i].number.toString()) {
                    storeExists = true;
                    break;
                }
            }
            if (!storeExists) {
                CONSTANTS.retailStaffingReq.showWaitCursor();
                // Call addhiringevent Store service
                var callbackFunction = $.Callbacks('once');
                callbackFunction.add(this.addHiringEventStoresResult);
                RSASERVICES.addHiringEventStores(callbackFunction, enterStrNum.val(), true, "Please wait...");
            } else {
            	storeExists = false;
                // Display warning message if store already exist
                CONSTANTS.retailStaffingReq.popup.alert("Store Number:" + enterStrNum.val() + " already exists in the list.");
                enterStrNum.val("");
            }
        }
    };
    /*
     * Hide \ UnHide controls
     */
    this.onSite_OffSiteShow = function() {
        CONSTANTS.offSiteEventFlg = $("input[name=offsiteEvent]:checked").val();
        $("#venueStateCbo").selectBoxIt();
        $("#venueStateCbo").data("selectBox-selectBoxIt").refresh();
        $("#venueStateCbo").empty();
        if (offSiteEventYes.prop('checked')) {
            eventStrNum.val("");
            eventAddress.text("");
            eventCity.text("");
            eventZip.text("");
            eventState.text("");
            var stateList = [];
            $.extend(stateList, CONSTANTS.reqStateDet);
            stateList.splice(0, 0, {
                'stateName' : 'Select...',
                'stateCode' : '0'
            });
            this.appendOptionToSelect('venueStateCbo', 'append', stateList, 'stateName', 'stateCode');
            $("#venueStateCbo").data("selectBox-selectBoxIt").refresh();
            // remove styles to controls
            offSiteEntryYes.removeClass('hide');
            // apply styles to controls
            offSiteEntryNo.addClass('hide');
            // Clear Store Details
            CONSTANTS.hiringEventStoreDetails = null;
            enterStrNum.val("");
        } else {
            // apply styles to controls
            offSiteEntryYes.addClass('hide');
            // remove styles to controls
            offSiteEntryNo.removeClass('hide');
            // Clear Off-Site Information
            venueName.val("");
            venueAddress.val("");
            venueCity.val("");
            $('#venueStateCbo option:first-child').attr("selected", "selected");
            $("#venueStateCbo").data("selectBox-selectBoxIt").refresh();
            venueZip.val("");
        }
    };
    this.addToolTip = function() {
        var selectBox = $("select + span ul li a");
        for (var i=0;i<selectBox.length;i++)
        {
            if($(selectBox[i]).text().length > CONSTANTS.SELECT_BOX_MAX_LENGTH) {
                $(selectBox[i]).attr("title",$(selectBox[i]).text());
            }
        }
    };
    /*
     * Check if offsite Event Venue Details 1.validate if venue name is not
     * empty 2.venue address is not empty 3.venue city is not empty 4.venue
     * state is not empty 5.venue Zip code is not empty 6.Display warning popup
     * if any of the above details are empty
     */
    this.offsiteEvntVenueDtlsCheck = function() {
        var isReturn = false;
        if (venueName.val() === "" || venueName.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.popup.alert("Venue Name is a required field.");
            venueName.focus().select();
            isReturn = true;
        } else if (venueAddress.val() === "" || venueAddress.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.popup.alert("Venue Address is a required field.");
            venueAddress.focus().select();
            isReturn = true;
        } else if (venueCity.val() === "" || venueCity.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.popup.alert("Venue City is a required field.");
            venueCity.focus().select();
            isReturn = true;
        } else if ($("#venueStateCbo option:selected").index() < 1) {
            CONSTANTS.retailStaffingReq.popup.alert("Venue State is a required field.");
            venueStateCbo.focus().select();
            isReturn = true;
        } else if (venueZip.val() === "" || venueZip.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.popup.alert("Venue Postal Code is a required field.");
            venueName.focus().select();
            isReturn = true;
        }
        return isReturn;
    };

    /*
     * Showing alerts if anything found invalid before Create 1.if event name is
     * empty 2.Event start date and end date is empty 3.Offsite event flag
     * should be available 4. Event store number is empty 5.Hiring event Manager
     * Data is empty 6. Hiring event Store details is empty Prompt warning
     * message if any of the mandatory values are missed
     */
    this.showAlertsBeforeCreate = function() {
        var isReturn = false;
        // Make sure that required fields are populated
        if (eventName.val() === "" || eventName.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.popup.alert("Event Name is a required field.");
            eventName.focus().select();
            isReturn = true;
        } else if (eventDt.val() === "" || eventDtEnd.val() === "") {
            CONSTANTS.retailStaffingReq.popup.alert("Event Date is a required field.");
            isReturn = true;
        }
        if (!isReturn) {
        	if (CONSTANTS.offSiteEventFlg === CONSTANTS.YES_VALUE) {
	            isReturn = CONSTANTS.retailStaffingReq.offsiteEvntVenueDtlsCheck();
	        } else if (eventStrNum.val() === "" || eventStrNum.val().length < 4) {
	            CONSTANTS.retailStaffingReq.popup.alert("Store Number is required for an On-Site Event");
	            eventStrNum.focus().select();
	            isReturn = true;
	        }
        	if (!isReturn) {
        		if (!CONSTANTS.hiringEventMgrData || !CONSTANTS.hiringEventMgrData.name) {
		            CONSTANTS.retailStaffingReq.popup.alert("Please enter a LDAP ID and click the Refresh Button.  Event Manager Data Required.");
		            isReturn = true;
		        } else if (CONSTANTS.HiringEventStoreList.length < 1) {
		            CONSTANTS.retailStaffingReq.popup.alert("There must be at least one Participating Store");
		            enterStrNum.focus().select();
		            isReturn = true;
		        }
		        // Make sure that the Host Store is in the Participating Store List
		        else {
		            isReturn = CONSTANTS.retailStaffingReq.validatePartStrList();
		        }
        	}
	        
        }
        
        return isReturn;
    };
    /*
     * Validate Part Store List If Offsite Event is No check if Hiring event
     * storelist is available if exist set host store in list if not exist
     * display warning message
     */
    this.validatePartStrList = function() {
        var isReturn = false;
        var hostStrInList = false;
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO_VALUE) {
            for ( var m = 0; m < CONSTANTS.HiringEventStoreList.length; m++) {
                if (CONSTANTS.HiringEventStoreList[m].number.toString() === eventStrNum.val().toString()) {
                    hostStrInList = true;
                }
            }
            if (!hostStrInList) {
                CONSTANTS.retailStaffingReq.popup.alert("The Store that the Event is being held at must be in the Participating Stores List.");
                isReturn = true;
            }
        }
        return isReturn;
    };
    /*
     * Create Hiring event
     */
    this.createHiringEvent = function() {
        CONSTANTS.offSiteEventFlg = $("input[name=offsiteEvent]:checked").val();
        var isAlertThrown = CONSTANTS.retailStaffingReq.showAlertsBeforeCreate();

        if (isAlertThrown) {
            return;
        }
        // Set the Event Data in a VO
        var hiringEventDetailsVO = {};
        hiringEventDetailsVO.hireEventName = eventName.val();
        hiringEventDetailsVO.hireEventBeginDate = this.convertDate(eventDt.val());
        hiringEventDetailsVO.hireEventEndDate = this.convertDate(eventDtEnd.val());

        // The Creating Store Number value
        hiringEventDetailsVO.strNumber = CONSTANTS.storeNo;

        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO_VALUE) {
            hiringEventDetailsVO.hireEventLocationDescription = CONSTANTS.hiringEventStoreDetails.name;
            hiringEventDetailsVO.hireEventAddressText = eventAddress.text();
            hiringEventDetailsVO.hireEventCityName = eventCity.text();
            hiringEventDetailsVO.hireEventStateCode = eventState.text();
            hiringEventDetailsVO.hireEventZipCodeCode = eventZip.text();
        } else {
            hiringEventDetailsVO.hireEventLocationDescription = venueName.val();
            hiringEventDetailsVO.hireEventAddressText = venueAddress.val();
            hiringEventDetailsVO.hireEventCityName = venueCity.val();
            hiringEventDetailsVO.hireEventStateCode = $('#venueStateCbo option:selected').val();
            hiringEventDetailsVO.hireEventZipCodeCode = venueZip.val();
        }

        hiringEventDetailsVO.emgrHumanResourcesAssociateId = CONSTANTS.hiringEventMgrData.emgrHumanResourcesAssociateId;
        hiringEventDetailsVO.phone = CONSTANTS.hiringEventMgrData.phone;
        hiringEventDetailsVO.eventMgrName = CONSTANTS.hiringEventMgrData.name;
        hiringEventDetailsVO.eventMgrTitle = CONSTANTS.hiringEventMgrData.title;
        hiringEventDetailsVO.eventMgrEmail = CONSTANTS.hiringEventMgrData.email;

        // Set the participating stores in the VO
        var tempArray = [];
        for ( var m = 0; m < CONSTANTS.HiringEventStoreList.length; m++) {
            var tempStores = {};
            tempStores.number = CONSTANTS.HiringEventStoreList[m].number;
            if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO_VALUE) {
                if (tempStores.number === hiringEventDetailsVO.strNumber) {
                    tempStores.isEventSiteStore = "Y";
                } else {
                    tempStores.isEventSiteStore = "N";
                }
            } else {
                tempStores.isEventSiteStore = "N";
            }
            tempArray.push(tempStores);
        }
        hiringEventDetailsVO.participatingStores = tempArray;

        var eventType = "";
        // Determine the Hiring Event Type
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.YES_VALUE) {
            // Off-Site Hiring Event
            eventType = "OSE";
        } else if (tempArray.length > 1) {
            // Multi-Store Event
            eventType = "MSE";
        } else {
            // Single Store Event
            eventType = "SSE";
        }

        hiringEventDetailsVO.hireEventTypeIndicator = eventType;

        CONSTANTS.HiringEventDetailsVO = hiringEventDetailsVO;
        CONSTANTS.retailStaffingReq.showWaitCursor();

        // Call the Check Hiring Event Name and Create Event
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.chkDuplicateHiringEvntNameResult);
        // Call duplicate Hiring Event service
        RSASERVICES.checkDuplicateHiringEventName(callbackFunction, hiringEventDetailsVO.hireEventName, true, "Please wait...");
    };
    /*
     * Add Hiring event based on stores response
     */
    this.addHiringEventStoresResult = function(data) {
        $.unblockUI();
        CONSTANTS.retailStaffingReq.showDefaultCursor();
        // Reset the values
        var returnedErrorMessage = "";
        var hiringEventStoreDetailsVO = {};
        // Spilt the store details
        for ( var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
            if (CONSTANTS.HiringEventStoreList[i].number === "") {
                CONSTANTS.HiringEventStoreList.splice(i, 1);
                i--;
            }
        }
        // Check for returned errors
        if (data.storeResponse.hasOwnProperty("error")) {
            var errorResultList = [];
            if (Array.isArray(data.storeResponse.error)) {
                errorResultList = data.storeResponse.error;
            } else {
                errorResultList = [ data.storeResponse.error ];
            }
            returnedErrorMessage = errorHandling(errorResultList);
            // Display Warning message
            CONSTANTS.retailStaffingReq.popup.alert(returnedErrorMessage);
        } else if (data.storeResponse.hasOwnProperty("valid")) {
            if (data.storeResponse.valid) {
                if (data.storeResponse.storeDetails) {
                    // Set service response to local instance
                    hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                    hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                    CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                    CONSTANTS.HiringEventStoreDetailsVO = hiringEventStoreDetailsVO;
                    if(!_.contains(_.pluck(CONSTANTS.HiringEventStoreList,"number"),hiringEventStoreDetailsVO.number)){
                    	CONSTANTS.HiringEventStoreList.push(hiringEventStoreDetailsVO);
                    }
                }
                // Build Store grid
                CONSTANTS.retailStaffingReq.buildStoresGrid(CONSTANTS.HiringEventStoreList);
            } else if (!data.storeResponse.valid) {
                // Display Alert message
                CONSTANTS.retailStaffingReq.popup.alert("Store/Location Number " + enterStrNum.val() + " is invalid.");
            }
        }
        enterStrNum.val("");
    };
    /*
     * Build Stores Grid details StoreNumber StoreName Delete store as button
     */
    this.buildStoresGrid = function(response) {
        $(".gridContainer").empty();
        CONSTANTS.retailStaffingReq.gridData = [];
        CONSTANTS.retailStaffingReq.gridData = response;

        $(".gridContainer").append("<div id='myGrid'></div>");
        var grid;
        var columns = [ {
            id : "number",
            sortable : false,
            name : "Store #",
            field : "number"
        }, {
            id : "name",
            sortable : false,
            name : "Store Name",
            field : "name"
        }, {
            id : "delete",
            sortable : false,
            name : "Delete Store",
            field : "name",
            formatter : function(row, cell, value) {
                return value ? ('<input type="button" id="delBtn_' + row + '" class="storeDelBtn" value="Delete">') : '';
            }
        } ];
        // Set forcefit as true so that column will split the space evenly
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            defaultSortAsc : true,
            forceFitColumns : true,
            scrollToTop : true,
            enableCellNavigation : true
        };
        var data = [];
        var tempData = CONSTANTS.retailStaffingReq.gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 3) {
            var emptyData = [];
            for ( var i = 0; i < 3; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        grid = new Slick.Grid("#myGrid", data, columns, options);
        $(window).resize(function() {
            $("#myGrid").width($(".gridContainer").width());
            $(".slick-viewport").width($("#myGrid").width());
            grid.resizeCanvas();
        });
        // onclick of delete store button call service
        $(".storeDelBtn").on("click", function(e) {
            CONSTANTS.retailStaffingReq.deleteStoreFromList(e);
        });
    };
    /*
     * Check Duplicate Hiring Event Name
     */
    this.chkDuplicateHiringEvntNameResult = function(data) {
        $.unblockUI();
        CONSTANTS.retailStaffingReq.showDefaultCursor();
        var returnedErrorMessage = "";
        CONSTANTS.HiringEventDetailsVO.stores = [];
        // Check for returned errors
        if (data.Response.hasOwnProperty("error")) {
            // Display warning message
            var errorResultList = [];
            if (Array.isArray(data.Response.error)) {
                errorResultList = data.Response.error;
            } else {
                errorResultList = [ data.Response.error ];
            }
            returnedErrorMessage = errorHandling(errorResultList);
            CONSTANTS.retailStaffingReq.popup.alert(returnedErrorMessage);
        } else if (data.Response.hasOwnProperty("status")) {
            // Check if all participating store has eventsite flag as true
            for ( var i = 0; i < CONSTANTS.HiringEventDetailsVO.participatingStores.length; i++) {
                if (CONSTANTS.HiringEventDetailsVO.participatingStores.length <= 1) {
                    CONSTANTS.HiringEventDetailsVO.stores = {};
                    CONSTANTS.HiringEventDetailsVO.stores.number = CONSTANTS.HiringEventDetailsVO.participatingStores[i].number;
                    CONSTANTS.HiringEventDetailsVO.stores.eventSiteFlg = CONSTANTS.HiringEventDetailsVO.participatingStores[i].isEventSiteStore;
                } else {
                    CONSTANTS.HiringEventDetailsVO.stores.push({
                        "number" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].number,
                        "eventSiteFlg" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].isEventSiteStore
                    });
                }

            }
            // Call the Hiring Event Create
            var createHiringRequest = {
                "CreateHiringEventRequest" : {
                    "hiringEventDetail" : {
                        "hiringEventType" : CONSTANTS.HiringEventDetailsVO.hireEventTypeIndicator,
                        "strNumber" : CONSTANTS.HiringEventDetailsVO.strNumber,
                        "eventName" : CONSTANTS.HiringEventDetailsVO.hireEventName,
                        "eventDt" : CONSTANTS.HiringEventDetailsVO.hireEventBeginDate,
                        "eventDtEnd" : CONSTANTS.HiringEventDetailsVO.hireEventEndDate,
                        "eventMgrName" : CONSTANTS.HiringEventDetailsVO.eventMgrName,
                        "eventMgrAssociateId" : CONSTANTS.HiringEventDetailsVO.emgrHumanResourcesAssociateId,
                        "eventMgrTitle" : CONSTANTS.HiringEventDetailsVO.eventMgrTitle,
                        "eventMgrPhone" : CONSTANTS.HiringEventDetailsVO.phone,
                        "eventMgrEmail" : CONSTANTS.HiringEventDetailsVO.eventMgrEmail,
                        "venueStoreName" : CONSTANTS.HiringEventDetailsVO.hireEventLocationDescription,
                        "eventAddress1" : CONSTANTS.HiringEventDetailsVO.hireEventAddressText,
                        "eventCity" : CONSTANTS.HiringEventDetailsVO.hireEventCityName,
                        "eventState" : CONSTANTS.HiringEventDetailsVO.hireEventStateCode,
                        "eventZip" : CONSTANTS.HiringEventDetailsVO.hireEventZipCodeCode
                    },
                    "participatingStores" : {
                        // Particiapting store
                        "store" : CONSTANTS.HiringEventDetailsVO.stores
                    }
                }
            };
            var inputData = {
                    xml : JSON.stringify({
                        "CreateHiringEventRequest" : createHiringRequest.CreateHiringEventRequest
                    })
                };
            CONSTANTS.retailStaffingReq.showWaitCursor();
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(CONSTANTS.retailStaffingReq.createHiringEventEventResult);
            // call create hiring event service
            RSASERVICES.createHiringEvent(callbackFunction, inputData, true, "Please wait...");
        }
    };
    /*
     * Create Hiring Event Results
     */
    this.createHiringEventEventResult = function(data) {
        $.unblockUI();
        CONSTANTS.retailStaffingReq.showDefaultCursor();
        var returnedErrorMessage = "";
        var schDescVo = {};
        // Check for returned errors
        if (data.Response.hasOwnProperty("error")) {
            var errorResultList = [];
            if (Array.isArray(data.Response.error)) {
                errorResultList = data.Response.error;
            } else {
                errorResultList = [ data.Response.error ];
            }
            returnedErrorMessage = errorHandling(errorResultList);
            CONSTANTS.retailStaffingReq.popup.alert(returnedErrorMessage);
        } else if (data.Response.hasOwnProperty("status")) {
            // Add new Hiring Event to drop down list
            var calId = data.Response.reqnCalId;
            var calType = CONSTANTS.HIRING_EVENT_CALENDAR_TYPE;
            var calDesc = CONSTANTS.HiringEventDetailsVO.hireEventName;

            schDescVo = {};
            schDescVo.requisitionCalendarDescription = calDesc;
            schDescVo.requisitionCalendarId = calId;
            schDescVo.type = calType;
            schDescVo.hireEventId = data.Response.hireEventId;
            schDescVo.enabled = true;
            CONSTANTS.interviewScheduleCollection.push(schDescVo);

            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            $('#calendarCbo').empty();
            CONSTANTS.retailStaffingReq.appendOptionToSelect('calendarCbo', 'append', CONSTANTS.interviewScheduleCollection, 'requisitionCalendarDescription', 'hireEventId');
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();

            // Select the Newly created Hiring Event
            for ( var m = 0; m < CONSTANTS.interviewScheduleCollection.length; m++) {
                if (calDesc === CONSTANTS.interviewScheduleCollection[m].requisitionCalendarDescription) {
                    $('#calendarCbo option:eq(' + (m) + ')').prop('selected', true);
                    $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                    CONSTANTS.retailStaffingReq.addToolTip();
                    break;
                }
            }
            CONSTANTS.retailStaffingReq.addToolTip();
            $('#createHiringEventPopUp').modal('hide');
            if($(".modal-backdrop.fade.in").length > 1)
                {
                $(".modal-backdrop.fade.in:eq(1)").css({"z-index":"1600"});
                }
            CONSTANTS.retailStaffingReq.popup.alert("Hiring Event Created");
        }
    };
    /*
     * Delete selected store from Participating Store List
     */
    this.deleteStoreFromList = function(e) {
        var indexDeleted = e.currentTarget.id.toString().substring(7);
        // Split the storelist
        CONSTANTS.HiringEventStoreList.splice(indexDeleted, 1);
        // Build Store grid
        CONSTANTS.retailStaffingReq.buildStoresGrid(CONSTANTS.HiringEventStoreList);
    };
    /*
     * Gets store details for an in-store hiring event
     */
    this.getStoreInfo = function(event) {
        if (eventStrNum.val().length === 4) {
            this.showWaitCursor();
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.getHiringEventStoreDetailsResult);
            // Add hiring event Stores
            RSASERVICES.addHiringEventStores(callbackFunction, eventStrNum.val(), true, "Please wait...");
            // Add to Participating Stores
            enterStrNum.val(eventStrNum.val());
        } else {
            CONSTANTS.hiringEventStoreDetails = null;
        }
    };
    /*
     * Get Hiring event Store details from service response
     */
    this.getHiringEventStoreDetailsResult = function(data) {
        $.unblockUI();
        CONSTANTS.retailStaffingReq.showDefaultCursor();
        var returnedErrorMessage = "";
        var hiringEventStoreDetailsVO = {};
        if (data.storeResponse.hasOwnProperty("valid")) {
            if (data.storeResponse.valid) {
                if (data.storeResponse.storeDetails) {
                    // Construct the response object to display in UI
                    CONSTANTS.hiringEventStoreDetails = null;
                    hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                    hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                    hiringEventStoreDetailsVO.address1 = data.storeResponse.storeDetails.addressLine1;
                    hiringEventStoreDetailsVO.city = data.storeResponse.storeDetails.city;
                    hiringEventStoreDetailsVO.state = data.storeResponse.storeDetails.state;
                    hiringEventStoreDetailsVO.zip = data.storeResponse.storeDetails.postalCode;
                    CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                    CONSTANTS.HiringEventStoreDetailsVO = hiringEventStoreDetailsVO;
                    eventAddress.text(hiringEventStoreDetailsVO.address1);
                    eventState.text(hiringEventStoreDetailsVO.state);
                    eventCity.text(hiringEventStoreDetailsVO.city);
                    eventZip.text(hiringEventStoreDetailsVO.zip);
                }
            } else if (!data.storeResponse.valid) {
                // Display warning message
                CONSTANTS.retailStaffingReq.popup.alert("Store/Location Number " + $("#eventStrNum").val() + " is invalid.");
                eventAddress.text("");
                eventCity.text("");
                eventZip.text("");
                eventState.text("");
                enterStrNum.val("");
                return;
            }
        } else if (data.storeResponse.hasOwnProperty("error")) {
            // Display warning message
            var errorResultList = [];
            if (Array.isArray(data.storeResponse.error)) {
                errorResultList = data.storeResponse.error;
            } else {
                errorResultList = [ data.storeResponse.error ];
            }
            returnedErrorMessage = errorHandling(errorResultList);
            CONSTANTS.retailStaffingReq.popup.alert(returnedErrorMessage);
        }
        CONSTANTS.retailStaffingReq.addStoreToHiringEvent();
    };
    /*
     * Refresh Manager Details
     */
    this.refreshManagerData = function() {
        if (eventMgrLdap.val() === "") {
            // Enter valida ldap
            CONSTANTS.retailStaffingReq.popup.alert("Please enter a LDAP ID.");
            return;
        } else {
            CONSTANTS.hiringEventMgrData = {};
            $("#mgrName").text("");
            $("#mgrTitle").text("");
            $("#mgrPhn").text("");
            $("#mgrMail").text("");
            CONSTANTS.retailStaffingReq.showWaitCursor();
        }
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getHiringEventMgrDataResult);
        // call gethiringEventManagerData
        RSASERVICES.getHiringEventMgrData(callbackFunction, eventMgrLdap.val().toUpperCase(), true, "Please wait...");

    };
    /*
     * get Hiring Event results from service response
     */
    this.getHiringEventMgrDataResult = function(data) {
        $.unblockUI();
        CONSTANTS.retailStaffingReq.showDefaultCursor();
        var returnedErrorMessage = "";
        eventMgrLdap.val("");
        // Check for returned errors
        // Clear Hiring Event Manager Data.
        CONSTANTS.hiringEventMgrData = {};
        if (data.Response.hasOwnProperty("hiringEventResponse") && data.Response.hiringEventResponse.hasOwnProperty("hiringEventMgrData")) {
             if (data.Response.hiringEventResponse.hiringEventMgrData) {
                // Display the value to UI controls
                CONSTANTS.hiringEventMgrData.name = data.Response.hiringEventResponse.hiringEventMgrData.name;
                CONSTANTS.hiringEventMgrData.title = data.Response.hiringEventResponse.hiringEventMgrData.title;
                CONSTANTS.hiringEventMgrData.phone = data.Response.hiringEventResponse.hiringEventMgrData.phone;
                CONSTANTS.hiringEventMgrData.email = data.Response.hiringEventResponse.hiringEventMgrData.email;
                CONSTANTS.hiringEventMgrData.emgrHumanResourcesAssociateId = data.Response.hiringEventResponse.hiringEventMgrData.associateId;
                $("#mgrName").text(CONSTANTS.hiringEventMgrData.name);
                $("#mgrTitle").text(CONSTANTS.hiringEventMgrData.title);
                $("#mgrPhn").text(CONSTANTS.hiringEventMgrData.phone);
                $("#mgrMail").text(CONSTANTS.hiringEventMgrData.email);
            }
        } else if (data.Response.hasOwnProperty("error")) {
            // Display warning message
            returnedErrorMessage = data.Response.error.errorMsg;
            CONSTANTS.retailStaffingReq.popup.alert(returnedErrorMessage);
            // Clear Hiring Event Manager Data.
            CONSTANTS.hiringEventMgrData = {};
        }
    };
    /*
     * Sets the selected range for Hiring Event End Date to start at the Begin
     * Date and greater.
     */
    this.setEndDate = function() {
        var minDate;
        var endDateBeginRange;
        if (eventDt.val() !== "") {
            minDate = new Date(eventDt.val().toString().substring(6), (eventDt.val().toString().substring(0, 2)) - 1, eventDt.val().toString().substring(5,3));
            endDateBeginRange = minDate;
            $("#eventDtEnd").datepicker( "option", "minDate", endDateBeginRange);
        }
        eventDtEnd.val(eventDt.val());
    };
    /*
     * Convert Date
     */
    this.convertDate = function(usDate) {
        var dateParts = usDate.split(/(\d{1,2})\/(\d{1,2})\/(\d{4})/);
        return dateParts[3] + "-" + dateParts[1] + "-" + dateParts[2];
    };
    /*
     * Remove backdrop blur and close alert popup on click of OK button.
     */
    this.alertOkClicked = function(id) {
        $(id).on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $(id).modal('hide');
    };
    /*
     * This function changes the cursor to auto
     *
     * @param N/A
     * @return N/A
     */
    this.showDefaultCursor = function() {
        $('html, body').css("cursor", "auto");
    };
    /*
     * This function changes the cursor to wait cursor
     *
     * @param N/A
     * @return N/A
     */
    this.showWaitCursor = function() {
        $('html, body').css("cursor", "wait");
    };

    $("input[data-pattern]").assignKeyDownToInputs();
}