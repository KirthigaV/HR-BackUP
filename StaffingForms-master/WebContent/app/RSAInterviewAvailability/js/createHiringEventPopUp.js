/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: createHiringEvent.js
 * Application: Staffing Administration
 *
 * Used to create Hiring event
 * This method gets create the hiring event and,
 *
 * @param inputXml
 * XML/JSON containing the form params and sent as request object to create calendar.
 * The request object is used to send as XML/JSON and returns
 * a HiringEventStoreList object.
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the hiring event
 * data provided to create.
 * This XML/JSON is generated and returns
 * HiringEventStoreList object
 */
/**
 * Method is used to create the hiring event and delete.
 * Rename action will be possible for some active hiring
 * events.
 * Validate the components.
 * This method will have hiring calendar summary with details
 * Can able to see the details of hiring slots
 */
function createHiringEvent() {
    /*
     * this will assign each component to a global variable where set values
     * anywhere in this page by using these variables
     *
     */
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
    /*
     * this function is used to initialize the create hiring event this will
     * empty all the labels initially
     */
    this.initialize = function() {
        /*
         * clear all the label values
         */
        eventAddress.empty();
        eventCity.empty();
        eventZip.empty();
        eventState.empty();
        $("#mgrName").empty();
        $("#mgrTitle").empty();
        $("#mgrPhn").empty();
        $("#mgrMail").empty();
        CONSTANTS.YES = "YES";
        CONSTANTS.NO = "NO";
        eventDt.val("");
        eventDtEnd.val("");
        $('#offSiteEventNo').prop('checked',true);
        this.startDateBeginRange = new Date();
        this.endDateBeginRange = new Date();
        this.setSelectableRange();
        CONSTANTS.retailStaffingReq = this;
        CONSTANTS.retailStaffingReq.gridData = [];
        /*
         * call to build grid
         */
        CONSTANTS.retailStaffingReq
                .buildStoresGrid(CONSTANTS.retailStaffingReq.gridData);
        CONSTANTS.retailStaffingReq.inputSelectText();

        /*
         * Clear Hiring Event Manager Data
         */
        CONSTANTS.hiringEventMgrData = null;
        CONSTANTS.interviewScheduleCollection = [];
        CONSTANTS.HiringEventStoreList = [];
        /* Clear Hiring Event Store Details */
        CONSTANTS.hiringEventStoreDetails = null;
        MODEL.EVENTSTORENUM="";
        offSiteEntryYes.addClass('hide');
        offSiteEntryNo.removeClass('hide');
        /*
         * this function is used to add the buttons to event start date and end
         * date
         */
        $('#eventDt, #eventDtEnd').datepicker({
            minDate : 1,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top"

        });      
        $('select:not(select[multiple])').selectBoxIt();
        $("#eventStrNum").attr("title",CONSTANTS.CREATE_HIRING_EVENT_STORE_NUMBER_TOOLTIP);       
    };
    /*
     * Method used to select, copy and
     * paste the text in input box.
     * Regex will be validated for accepting
     * few sepcial characters.
     * 
     */
    this.inputSelectText = function(){
    	/*
     	 * Copy and paste the selected event name text
     	 * It allows some special characters with alphabets
     	 */
		 $("#eventName").off('paste');
		 $("#eventName").on('paste', function(e) {
			 setTimeout(function(){
				 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
				 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected event manager ldap text
     	  * It allows some special characters with alphabets
     	  */
		 $("#eventMgrLdap").off('paste');
		 $("#eventMgrLdap").on('paste', function(e) {
			 setTimeout(function(){
				 var tempNum = $(e.currentTarget).val().match(/[0-9a-zA-ZÀ]+/g);
				 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected event name text
     	  * It allows some special characters with alphabets
     	  */
		 $("#venueName").off('paste');
		 $("#venueName").on('paste', function(e) {
		 setTimeout(function(){
		     var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
		     $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected venue address text
     	  * It allows some special characters with alphabets
     	  */
		 $("#venueAddress").off('paste');
		 $("#venueAddress").on('paste', function(e) {
		 setTimeout(function(){
		     var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
		     $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected venue city text
     	  * It allows some special characters with alphabets
     	  */
		 $("#venueCity").off('paste');
		 $("#venueCity").on('paste', function(e) {
		 setTimeout(function(){
		     var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
		     $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected venue zip text
     	  * It allows some special characters with alphabets
     	  */
		 $("#venueZip").off('paste');
		 $("#venueZip").on('paste', function(e) {
		 setTimeout(function(){
		      var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
		      $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * Copy and paste the selected event store text
     	  * It allows some special characters with alphabets
     	  */
		 $("#eventStrNum").off('paste');
		 $("#eventStrNum").on('paste', function(e) {
		 setTimeout(function(){
		     var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
		     $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
		      },100);
		 });
		 /*
     	  * Copy and paste the selected participating store text
     	  * It allows some special characters with alphabets
     	  */
		 $("#enterStrNum").off('paste');
		 $("#enterStrNum").on('paste', function(e) {
		 setTimeout(function(){
		     var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
		     $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
		     },100);
		 });
		 /*
     	  * keyup function called for event store num     	 
     	  */
		 $("#eventStrNum").off("keyup");
		 $("#eventStrNum").on('keyup', function(e) {
		     CONSTANTS.retailStaffingReq.getStoreInfo(e);
		 });
    };
    /*
     * this function is called when we select yes for offsite event it is used
     * to append the values to state drop down
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray,
            textKey, valueKey) {
        if (action === "append") {
            $("#venueStateCbo + span ul li:contains('Select')").empty();
            for (var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
            }
            //Refresh and set the data in dropdown
            $('#' + drpDownId).attr('disabled', false);
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
            if ($('#' + drpDownId).data("selectBox-selectBoxIt")) {
                $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
            }
            $("#venueStateCbo + span ul li:contains('Select')").empty();
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
        }

    };
    /*
     * this method is used to focus the event start date
     */
    this.focusFromDate = function() {
        $('#eventDt').focus();
    };
    /*
     * this method is used to focus the event end date
     */
    this.focusToDate = function() {
        $('#eventDtEnd').focus();
    };
    /*
     * this function is used to add the stores to the hiring event
     */
    this.addStoreToHiringEvent = function() {
    	MODEL.EVENTSTORENUM = enterStrNum.val();
        var storeExists = false;
        if (enterStrNum.val().length === 4) {
            /*
             * Make sure that the requested Store is not already in the list
             */
            for (var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
                if (enterStrNum.val().toString() === CONSTANTS.HiringEventStoreDetailsVO[i].number.toString()) {
                    storeExists = true;
                    break;
                }
            }
            /*
             * if store is not exist then call services to show the store
             * details say address,state,city.
             */
            if (!storeExists) {
                var callbackFunction = $.Callbacks('once');
                this.addHiringEventStoresURL = CONSTANTS.addHiringEventStoresURL;
                callbackFunction.add(this.addHiringEventStoresResult);
                RSASERVICES.addHiringEventStores(callbackFunction,
                        this.addHiringEventStoresURL, enterStrNum.val(), true,
                        "Please wait...");
            }
            /*
             * show alert if store number is already present
             */
            else {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody").text(
                        "Store Number:" + enterStrNum.val()
                                + " already exists in the list.");
                createHiringEventObj.addModalCss();
                enterStrNum.val("");
            }
        }
    };
    /*
     * this function is called based on the event is offsite or onsite
     */
    this.onSite_OffSiteShow = function() {
        eventAddress.empty();
        eventCity.empty();
        eventZip.empty();
        eventState.empty();
        venueName.val("");
        venueAddress.val("");
        venueCity.val("");
        venueZip.val("");
        //validate the checkbox
        if ($("input[name=offsiteEvent]:checked").attr("id") === "offSiteEventYes") {
            CONSTANTS.offSiteEventFlg = "YES";
        } else {
            CONSTANTS.offSiteEventFlg = "NO";
        }
        this.appendOptionToSelect('venueStateCbo', 'append',
                CONSTANTS.reqStateDet, 'stateName', 'stateCode');
        /*
         * if it is offsitevent show the corresponding divs
         */
        if (offSiteEventYes.prop('checked')) {
            offSiteEntryYes.removeClass('hide');
            offSiteEntryNo.addClass('hide');
            // Clear Store Details
            CONSTANTS.hiringEventStoreDetails = null;
            eventStrNum.val("");
        }
        /*
         * if it is a onsite event show the corresponsing divs
         */
        else {
            offSiteEntryYes.addClass('hide');
            offSiteEntryNo.removeClass('hide');
            // Clear Off-Site Information
            venueName.val("");
            venueAddress.val("");
            venueCity.val("");
            $('#venueStateCbo option:first-child').attr("selected", "selected");
            venueZip.val("");
        }
    };
    /*
     * this function is used for validation while creating
     * the hiring event this
     * will check the manager details and store list
     */
    this.checkHiringEventStoreList = function() {
        /*
         * if hiringEventMgrData is null show the prompt Manager data required
         */
         if (!CONSTANTS.hiringEventMgrData || !CONSTANTS.hiringEventMgrData.name) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody")
                    .text(
                            "Please enter a LDAP ID and click the Refresh Button.  Event Manager Data Required.");
            createHiringEventObj.addModalCss();
            return true;
        }
        /*
         * if HiringEventStoreList is null show the promptatleast one
         * participating store
         */
        if (CONSTANTS.HiringEventStoreList.length < 1) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "There must be at least one Participating Store");
            enterStrNum.focus();
            createHiringEventObj.addModalCss();
            return true;
        }
        return false;
    };
    /*
     * this function is used for validation while creating the hiring event this
     * will check the store is being held in the participating store or not
     */
    this.validateCreateHiringEvent = function() {
        var hostStrInList = false;
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
            for (var m = 0; m < CONSTANTS.HiringEventStoreList.length; m++) {
            	if (CONSTANTS.HiringEventStoreList[m].number.toString() === eventStrNum.val().toString()) {
                    hostStrInList = true;
                }
            }
            /*
             * The store number created should present in participating stores,
             * else show the prompt
             */
            if (!hostStrInList) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody")
                        .text(
                                "The Store that the Event is being held at must be in the Participating Stores List.");
                createHiringEventObj.addModalCss();
                $("#createAlertPopupModal .alertModalBody").removeClass(
                        "createHiringEventalignModal");
                return true;
            }
        }
        return false;
    };
    /*
     * this function is used to frame the request send for create hiring event
     * service
     */
    this.sethiringEventDetailsVO = function() {
        /*
         * Set the Event Data in a VO
         */
        var hiringEventDetailsVO = {};
        hiringEventDetailsVO.hireEventName = eventName.val();
        hiringEventDetailsVO.hireEventBeginDate = this.convertDate(eventDt
                .val());
        hiringEventDetailsVO.hireEventEndDate = this.convertDate(eventDtEnd
                .val());
        /*
         * The Creating Store Number value
         */
        hiringEventDetailsVO.strNumber = MODEL.StoreNumber;
        /*
         * compare the flags and get the value based
         * on flag response
         */
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
            hiringEventDetailsVO.hireEventLocationDescription = CONSTANTS.hiringEventStoreDetails.name;
            hiringEventDetailsVO.hireEventAddressText = eventAddress.text();
            hiringEventDetailsVO.hireEventCityName = eventCity.text();
            hiringEventDetailsVO.hireEventStateCode = eventState.text();
            hiringEventDetailsVO.hireEventZipCodeCode = eventZip.text();
        } else {
            hiringEventDetailsVO.hireEventLocationDescription = venueName.val();
            hiringEventDetailsVO.hireEventAddressText = venueAddress.val();
            hiringEventDetailsVO.hireEventCityName = venueCity.val();
            hiringEventDetailsVO.hireEventStateCode = $(
                    '#venueStateCbo option:selected').val();
            hiringEventDetailsVO.hireEventZipCodeCode = venueZip.val();
        }
        //set the values from CONSTANTS.hiringEventMgrData to hiringEventDetailsVO
        hiringEventDetailsVO.emgrHumanResourcesAssociateId = CONSTANTS.hiringEventMgrData.emgrHumanResourcesAssociateId;
        hiringEventDetailsVO.phone = CONSTANTS.hiringEventMgrData.phone;
        hiringEventDetailsVO.eventMgrName = CONSTANTS.hiringEventMgrData.name;
        hiringEventDetailsVO.eventMgrTitle = CONSTANTS.hiringEventMgrData.title;
        hiringEventDetailsVO.eventMgrEmail = CONSTANTS.hiringEventMgrData.email;
        /*
         * Set the participating stores in the VO
         */
        hiringEventDetailsVO.participatingStores = this.setTempStoreList();
        /*
         * Determine the Hiring Event Type
         */
        hiringEventDetailsVO.hireEventTypeIndicator = this
                .setEventType(hiringEventDetailsVO.participatingStores);
        CONSTANTS.HiringEventDetailsVO = hiringEventDetailsVO;
        /*
         * call the Check Hiring Event Name and Create Event
         */
        var callbackFunction = $.Callbacks('once');
        this.checkDuplicateHiringEventNameURL = CONSTANTS.checkDuplicateHiringEventNameURL;
        callbackFunction.add(this.chkDuplicateHiringEvntNameResult);
        RSASERVICES.checkDuplicateHiringEventName(callbackFunction,
                this.checkDuplicateHiringEventNameURL,
                hiringEventDetailsVO.hireEventName);

    };
    /*
     * this function is used to frame participating store object it will assign
     * the value isEventSiteStore as Y or N based on a condition
     */
    this.setTempStoreList = function() {
        var tempArray = [];
        for (var storeList = 0; storeList < CONSTANTS.HiringEventStoreList.length; storeList++) {
            var tempStores = {};
            tempStores.number = CONSTANTS.HiringEventStoreList[storeList].number;
            /*
             * iseventsitestore is Y if it is the created store if it is not a
             * created store and only it is a participating store set as 'N'
             */
            if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
                if (tempStores.number === MODEL.StoreNumber) {
                    tempStores.isEventSiteStore = "Y";
                } else {
                    tempStores.isEventSiteStore = "N";
                }
            } else {
                tempStores.isEventSiteStore = "N";
            }
            tempArray.push(tempStores);
        }
        return tempArray;
    };
    /*
     * this function is used to set the event type
     */
    this.setEventType = function(tempArray) {
        var eventType = "";
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.YES) {
            // Off-Site Hiring Event
            eventType = "OSE";
        } else if (tempArray.length > 1) {
            // Multi-Store Event
            eventType = "MSE";
        } else {
            // Single Store Event
            eventType = "SSE";
        }
        return eventType;
    };
    /*
     * this function is used validate the event start date and end date and
     * event name
     */
    this.checkEventDt = function() {
        /*
         * check for the event name if it is null show the prompt to enter event
         * name
         */
        if (eventName.val() === ""  || eventName.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Event Name is a required field.");
            eventName.focus();
            return true;
        }
        /*
         * check for event date if it is null show the prompt to select the date
         */
        if (eventDt.val() === "" || eventDtEnd.val() === "") {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Event Date is a required field.");
            return true;
        }
        return false;
    };
    /*
     * this function is used validate the venue city,venuestate and venue zip
     */
    this.checkVenues = function() {
        /*
         * check for venue city if it is null show the prompt to provide the
         * venue city
         */
        if (venueCity.val() === "" || venueCity.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Venue City is a required field.");
            venueCity.focus();
            return true;
        }
        /*
         * check for event state.If none of the state selected then prompt to
         * select the state from the drop down
         */
        if ($("#venueStateCbo option:selected").index() === -1) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Venue State is a required field.");
            venueStateCbo.focus();
            return true;
        }
        /*
         * check for event venue zip if it is null show the prompt toenter
         * postal code
         */
        if (venueZip.val() === "" || venueZip.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Venue Postal Code is a required field.");
            venueName.focus();
            createHiringEventObj.addModalCss();
            $("#createAlertPopupModal .alertModalBody").removeClass(
                    "createHiringEventalignModal");
            $("#createAlertPopupModal .modal-body ").removeClass(
                    "createHiringEventlargeBody");
            return true;
        }
        return false;
    };
    /*
     * this function is used to validate whether event is offsite or onsite
     * based on that validation for the corresponding fields are shown
     */
    this.checkOffSiteEventFlg = function() {
        /*
         * the event is a offsite event
         */
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.YES) {
            /*
             * checks for the venue name if it is null prompt to eneter the
             * venue name
             */
            if (venueName.val() === "" || venueName.val().trim().length <= 0) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody").text(
                        "Venue Name is a required field.");
                venueName.focus();
                return true;
            }
            /*
             * checks for the venue address if it is null prompt to eneter the
             * venue address
             */
            if (venueAddress.val() === "" || venueAddress.val().trim().length <= 0) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody").text(
                        "Venue Address is a required field.");
                venueAddress.focus();
                return true;
            }
            /*
             * check venues will check the city,state and zip venues are null or
             * not
             */
            var check = true;
            check = this.checkVenues();
            if (check) {
                return true;
            }
        }
        /*
         * the event is onsite
         */
        else {
            /*
             * checkForeventstrnum returns true if store number is valid
             */
            if (this.checkForeventStrNum()) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody").text(
                        "Store Number is required for an On-Site Event");
                $("#createAlertPopupModal .modal-content").addClass(
                        "createHiringEventlargeModal");
                $("#createAlertPopupModal .alertModalBody").addClass(
                        "createHiringEventalignModal");
                $('#createAlertPopupModal').on('hidden.bs.modal', function() {
                    createHiringEventObj.removeCreateAlertCss();
                });
                eventStrNum.focus();
                return true;
            }
        }
        return false;
    };
    /*
     * this function is used validate the store number for an onsite event
     */
    this.checkForeventStrNum = function() {
        return (eventStrNum.val() === "" || eventStrNum.val().length < 4);
    };
    /*
     * this function is used to set constants based on the selection of
     * offsite/onsite
     */
    this.setoffSiteEventFlg = function() {
        if ($("input[name=offsiteEvent]:checked").attr("id") === "offSiteEventYes") {
            CONSTANTS.offSiteEventFlg = "YES";
        } else {
            CONSTANTS.offSiteEventFlg = "NO";
        }
    };
    /*
     * this method is used to call several methods to validate all mandatory
     * fields
     */
    this.createHiringEvent = function() {
        /*
         * Make sure that required fields are populated
         */
        var cont = true;
        this.setoffSiteEventFlg();
        /*
         * set offsite event flag based on selected radio button by calling
         * function checkEventDt
         */
        cont = this.checkEventDt();
        if (cont) {
            return;
        }
        cont = true;
        cont = this.checkOffSiteEventFlg();
        if (cont) {
            return;
        }
        /*
         * check store list
         */
        cont = true;
        cont = this.checkHiringEventStoreList();
        if (cont) {
            return;
        }
        /*
         * validate the fields
         */
        cont = true;
        cont = this.validateCreateHiringEvent();
        if (cont) {
            return;
        }
        /*
         * form the hiring event details vo
         */
        this.sethiringEventDetailsVO();
    };
    /*
     * this function is used to add the store list in to the grid
     */
    this.addHiringEventStoresResult = function(data) {
        var returnedErrorMessage = "";
        var hiringEventStoreDetailsVO = {};
        /*
         * Iterate the storelist
         */
        for (var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
            if (CONSTANTS.HiringEventStoreList[i].number === "") {
                CONSTANTS.HiringEventStoreList.splice(i, 1);
                i--;
            }
        }
        /*
         * if store number is valid add store into store list
         */
        if (data.storeResponse.hasOwnProperty("valid")) {
            if (data.storeResponse.valid) {
                if (data.storeResponse.storeDetails) {
                    hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                    hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                    CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                    if(!_.contains(_.pluck(CONSTANTS.HiringEventStoreList,"number"),hiringEventStoreDetailsVO.number)){
                        CONSTANTS.HiringEventStoreList
                        .push(hiringEventStoreDetailsVO);
                    }
                    CONSTANTS.HiringEventStoreDetailsVO = CONSTANTS.HiringEventStoreList;
                }
                CONSTANTS.retailStaffingReq
                .buildStoresGrid(CONSTANTS.HiringEventStoreList);
            } else if (!data.storeResponse.valid) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#createAlertPopupModal .alertModalBody").text(
                        "Store/Location Number " + MODEL.EVENTSTORENUM
                                + " is invalid.");
                createHiringEventObj.addModalCss();
                $("#createAlertPopupModal .modal-body ").removeClass(
                        "createHiringEventlargeBody");
                $("#createAlertPopupModal .modal-body ").addClass("invalidStore");
            }
        }
        /*
         * check for return errors
         */
        else if (data.storeResponse.hasOwnProperty("error")) {
            if (_.isArray(data.storeResponse.error)) {
                returnedErrorMessage = data.storeResponse.error;
            } else {
                returnedErrorMessage = [ data.storeResponse.error ];
            }
            var errorMessage = MODEL.getErrorMessage(returnedErrorMessage);
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .modal-body").addClass("autoHeight");
            $("#createAlertPopupModal .alertModalBody").text(
                    errorMessage);
        }
        enterStrNum.val("");
    };
    /*
     * this function is used to build the grid
     */
    this.buildStoresGrid = function(response) {
        CONSTANTS.retailStaffingReq.gridData = [];
        CONSTANTS.retailStaffingReq.gridData = response;
        var grid;
        var columns = [ {
            /*
             * this column is used to show the store number
             */
            id : "number",
            sortable : false,
            width : 100,
            name : "Store #",
            field : "number"
        },
        /*
         * this column is used to show the store name
         */
        {
            id : "name",
            sortable : false,
            width : 220,
            name : "Store Name",
            field : "name"
        },
        /*
         * this column is used to show the delete button
         */
        {
            id : "delete",
            sortable : false,
            width : 230,
            name : "Delete Store",
            field : "name",
            formatter : this.deleteButtonFormatter
        } ];
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true,
            enableCellNavigation : true
        };
        var data = [];
        var tempData = CONSTANTS.retailStaffingReq.gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        $("#storescreategrid").css('width','580px');
        grid = new Slick.Grid("#storescreategrid", data, columns, options);
        $(window).resize(function() {
            $("#storescreategrid").width($(".gridContainer").width());
            $(".slick-viewport").width($("#myGrid").width());
            grid.resizeCanvas();
        });
        //click on delete button
        $(".storeDelBtn").on("click", function(e) {
            CONSTANTS.retailStaffingReq.deleteStoreFromList(e);
        });
    };
    /*
     * this function is used to add a delete button in a row of the grid
     */
    this.deleteButtonFormatter = function(row, cell, value) {
        return value ? ('<input type="button" id="delBtn_' + row + '" class="storeDelBtn" value="Delete">')
                : '';
    };
    /*
     * this function is the success handler of check duplicate hiring event
     * service this will frame a request to send cretae hiring event service
     * call
     */
    this.chkDuplicateHiringEvntNameResult = function(data) {
        var returnedErrorMessage = "";
        /*
         * form request to create hiring event
         */
        if (data.Response.hasOwnProperty("status")) {
            if (CONSTANTS.HiringEventDetailsVO.participatingStores.length === 1) {
                CONSTANTS.HiringEventDetailsVO.stores = {};
                CONSTANTS.HiringEventDetailsVO.stores = {
                    "number" : CONSTANTS.HiringEventDetailsVO.participatingStores[0].number,
                    "eventSiteFlg" : CONSTANTS.HiringEventDetailsVO.participatingStores[0].isEventSiteStore
                };
            } else {
                CONSTANTS.HiringEventDetailsVO.stores = [];
                for (var i = 0; i < CONSTANTS.HiringEventDetailsVO.participatingStores.length; i++) {

                    CONSTANTS.HiringEventDetailsVO.stores
                            .push({
                                "number" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].number,
                                "eventSiteFlg" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].isEventSiteStore
                            });
                }
            }
            /*
             * Call the Hiring Event service
             */
            var createHiringRequest = {
                "CreateHiringEventRequest" : {
                    "hiringEventDetail" : {
                        "hiringEventType" : CONSTANTS.HiringEventDetailsVO.hireEventTypeIndicator,
                        "strNumber" : MODEL.StoreNumber,
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
                        "store" : CONSTANTS.HiringEventDetailsVO.stores
                    }
                }
            };
            var callbackFunction = $.Callbacks('once');
            callbackFunction
                    .add(CONSTANTS.retailStaffingReq.createHiringEventEventResult);
            this.createHiringEventURL = CONSTANTS.createHiringEventURL;
            RSASERVICES.createHiringEvent(callbackFunction,
                    this.createHiringEventURL, createHiringRequest);
        }
        /*
         * check for returned errors
         */
        else if (data.Response.hasOwnProperty("error")) {
            if (_.isArray(data.Response.error)) {
                returnedErrorMessage = data.Response.error;
            } else {
                returnedErrorMessage = [ data.Response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(returnedErrorMessage);
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .modal-body").addClass("autoHeight");
            $("#createAlertPopupModal .alertModalBody").text(
                    errorMessage);
        }
    };
    /*
     * this function is the success handler for create hiring event.
     */
    this.createHiringEventEventResult = function(data) {
        var returnedErrorMessage = "";
        /*
         * if status is success show the prompt as hiring event created
         */
        if (data.Response.status === "SUCCESS") {
            $("#createHiringEventPopUp").modal("hide");
            $("#createAlertPopupModal").modal("hide");
            $("#createAlertPopupModal .modal-body").addClass("confirmedPopupHeight");
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Hiring Event Created");
            hiringEvent.loadData();
        }
        /*
         * check for returned errors
         */
        else if (data.Response.hasOwnProperty("error")) {
            if (_.isArray(data.Response.error)) {
                returnedErrorMessage = data.Response.error;
            } else {
                returnedErrorMessage = [ data.Response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(returnedErrorMessage);
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .modal-body").addClass("autoHeight");
            $("#createAlertPopupModal .alertModalBody").text(
                    errorMessage);
        }
    };
    /*
     * this function is the success handler for create hiring event.
     */
    this.deleteStoreFromList = function(e) {
        var indexDeleted = e.currentTarget.id.substring(7);
        CONSTANTS.HiringEventStoreList.splice(indexDeleted, 1);
        CONSTANTS.retailStaffingReq.buildStoresGrid(CONSTANTS.HiringEventStoreList);
    };    
    /*
     * Gets store details for an in-store hiring event
     */
    this.getStoreInfo = function() {
        eventAddress.empty();
        eventCity.empty();
        eventZip.empty();
        eventState.empty();
        if (eventStrNum.val().length === 4) {
            var callbackFunction = $.Callbacks('once');
            this.addHiringEventStoresURL = CONSTANTS.addHiringEventStoresURL;
            callbackFunction.add(this.getHiringEventStoreDetailsResult);
            RSASERVICES.addHiringEventStores(callbackFunction,
                    this.addHiringEventStoresURL, eventStrNum.val(), true,
                    "Please wait...");
            /*
             * Add to Participating Stores
             */
            MODEL.EVENTSTORENUM = eventStrNum.val();
            enterStrNum.val(eventStrNum.val());
            /*this.addStoreToHiringEvent(eventData);*/
        } else {
            CONSTANTS.hiringEventStoreDetails = null;
        }     
    };
    /*
     * this function is the success handler for store details service call
     */
    this.getHiringEventStoreDetailsResult = function(data) {
        var returnedErrorMessage = "";
        var hiringEventStoreDetailsVO = {};
        /*
         * if the store details is valid assign values and show in pop up
         */
        if (data.storeResponse.hasOwnProperty("valid")) {
            if (data.storeResponse.valid
                    && data.storeResponse.storeDetails) {
                CONSTANTS.hiringEventStoreDetails = null;
                //add the values from response data to hiringEventStoreDetailsVO object
                hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                hiringEventStoreDetailsVO.address1 = data.storeResponse.storeDetails.addressLine1;
                hiringEventStoreDetailsVO.city = data.storeResponse.storeDetails.city;
                hiringEventStoreDetailsVO.state = data.storeResponse.storeDetails.state;
                hiringEventStoreDetailsVO.zip = data.storeResponse.storeDetails.postalCode;
                CONSTANTS.hiringEventStoreDetails = hiringEventStoreDetailsVO;
                eventAddress.text(hiringEventStoreDetailsVO.address1);
                eventState.text(hiringEventStoreDetailsVO.state);
                eventCity.text(hiringEventStoreDetailsVO.city);
                eventZip.text(hiringEventStoreDetailsVO.zip);
            }
            //check if response has not valid
            else if (!data.storeResponse.valid) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                createHiringEventObj.addModalCss();
                $("#createAlertPopupModal .modal-body ").removeClass(
                        "createHiringEventlargeBody");
                $("#createAlertPopupModal .modal-body ").addClass("invalidStore");
                $("#createAlertPopupModal .alertModalBody").text(
                        "Store/Location Number " +$("#eventStrNum").val()
                                + " is invalid.");
                eventAddress.text("");
                eventCity.text("");
                eventZip.text("");
                eventState.text("");
                enterStrNum.val("");
                return;
            }
        }
        /*
         * check for returned errors
         */
        else if (data.storeResponse.hasOwnProperty("error")) {
            if (_.isArray(data.storeResponse.error)) {
                returnedErrorMessage = data.storeResponse.error;
            } else {
                returnedErrorMessage = [ data.storeResponse.error ];
            }
            var errorMessage = MODEL.getErrorMessage(returnedErrorMessage);
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .modal-body").addClass("autoHeight");
            $("#createAlertPopupModal .alertModalBody").text(
                    errorMessage);
        }
        CONSTANTS.retailStaffingReq.addStoreToHiringEvent();
    };
    /*
     * This function is called when refresh button is
     * clicked and hit the
     * service to get manager details
     */
    this.refreshManagerData = function() {
        /*
         * if manager text box is empty shows the prompt
         */
        if (eventMgrLdap.val() === "") {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .alertModalBody").text(
                    "Please enter a LDAP ID.");
            return;
        }
        //service call back to get manager detail
        var callbackFunction = $.Callbacks('once');
        this.getHiringEventMgrDataURL = CONSTANTS.getHiringEventMgrDataURL;
        callbackFunction.add(this.getHiringEventMgrDataResult);
        RSASERVICES
                .getHiringEventMgrData(callbackFunction,
                        this.getHiringEventMgrDataURL, eventMgrLdap.val()
                                .toUpperCase());
    };
    /*
     * this function is the success handler of gethiringeventmgrdata where it
     * will populate the manager details in the popup
     */
    this.getHiringEventMgrDataResult = function(data) {
        var returnedErrorMessage = "";
        eventMgrLdap.val("");
        /*
         * check for returned errors
         */
        if (data.Response.hasOwnProperty("error")) {
            returnedErrorMessage = data.Response.error.errorMsg;
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#createAlertPopupModal .modal-body").addClass("autoHeight");
            $("#createAlertPopupModal .alertModalBody").text(
                    returnedErrorMessage);
            /*
             * Clear Hiring Event Manager Data.
             */
            CONSTANTS.hiringEventMgrData = {};
            $("#mgrName").text("");
            $("#mgrTitle").text("");
            $("#mgrPhn").text("");
            $("#mgrMail").text("");
        } else if (data.Response.hiringEventResponse
                .hasOwnProperty("hiringEventMgrData")) {
            /*
             * Clear Hiring Event Manager Data.
             */
            CONSTANTS.hiringEventMgrData = {};
            if (data.Response.hiringEventResponse.hiringEventMgrData != null) {
                //add the values from response data to  CONSTANTS.hiringEventMgrData object
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
        }

    };
    /*
     * Sets the selected range for Hiring Event End Date to start at the Begin
     * Date and greater.
     */
    this.setEndDate = function() {
        var minDate;
        if (eventDt.val() !== "") {
        	minDate = new Date(eventDt.val().toString().substring(6), (eventDt.val().toString().substring(0, 2)) - 1, eventDt.val().toString().substring(5,3));
            endDateBeginRange = minDate;
            $("#eventDtEnd").datepicker( "option", "minDate", endDateBeginRange);
        }
        eventDtEnd.val(eventDt.val());
    };
    /*
     * this function is used to convert the selected date in picker into
     * dd/mm/yyyy
     */
    this.convertDate = function(usDate) {
        var dateParts = usDate.split(/(\d{1,2})\/(\d{1,2})\/(\d{4})/);
        return dateParts[3] + "-" + dateParts[1] + "-" + dateParts[2];
    };
    /*
     * Sets the selected range for Hiring Event Start and End Date to start
     * tomorrow. User cannot select today.
     */
    this.setSelectableRange = function() {
        var minDate = new Date();
        minDate.setDate(minDate.getDate() + 1);
        this.startDateBeginRange = minDate;
        this.endDateBeginRange = minDate;
    };
    /*
     * this function is used to close alert popup and
     * remove css for that popups
     */
    this.Okclicked = function() {
        $("#createAlertPopupModal").modal("hide");
    };
    /*
     * Method has css class to remove
     * for createAlertPopupModal popup
     */
    this.removeCreateAlertCss = function() {
        $('#createAlertPopupModal').unbind('hidden.bs.modal');
        $("#createAlertPopupModal .modal-content").removeClass(
                "createHiringEventlargeModal");
        $("#createAlertPopupModal .alertModalBody").removeClass(
                "createHiringEventalignModal");
        $("#createAlertPopupModal .modal-body ").removeClass(
                "createHiringEventlargeBody");
        $("#createAlertPopupModal .modal-body").removeClass("confirmedPopupHeight");
        $("#createAlertPopupModal .modal-body ").removeClass("invalidStore");
        $("#createAlertPopupModal .modal-body").removeClass("autoHeight");
    };
    /*
     * Method has css class to add
     * for createAlertPopupModal popup
     */
    this.addModalCss = function() {
        $("#createAlertPopupModal .modal-content").addClass(
                "createHiringEventlargeModal");
        $("#createAlertPopupModal .modal-body ").addClass(
                "createHiringEventlargeBody");
        $("#createAlertPopupModal .alertModalBody").addClass(
                "createHiringEventalignModal");
        $('#createAlertPopupModal').on('hidden.bs.modal', function() {
            createHiringEventObj.removeCreateAlertCss();
        });
    };
    /*
     * Method for on click of cancel button in hiring event
     * popup.
     * Popup content should get scrolled up and
     * radio button should get reset
     */
    this.closepopup = function(){
        $("#createHiringEventPopUp .modal-body").scrollTop(0);
        $('#offSiteEventNo').prop('checked',true);
        $('#offSiteEventYes').prop('checked',false);
        $('#createHiringEventPopUp').modal("hide");
   };
   /*
    * Method is used to show and drag the
    * popup.
    */
   this.setFocusandDraggable = function(){
       $('#createAlertPopupModal').off("shown.bs.modal");
       $('#createAlertPopupModal').on("shown.bs.modal", function() {
           $("#createHiringButton").focus();
       });
       CONSTANTS.retailStaffingReq.blockPopup('#createAlertPopupModal');
       CONSTANTS.retailStaffingReq.removeCreateAlertCss();
       $("#createAlertPopupModal").draggable({
            handle: ".modal-header"
        });
   };
   /*
    * Method is used to block the background on
    * showing popup orelse on click of outside
    * the popup will be closed
    *
    * Method to block the background
    * and cannot be able to handle the
    * keyboards events
    */
   this.blockPopup = function(popupname) {
       $(popupname).modal({
           backdrop : 'static',
           keyboard : false
       });
   };
}