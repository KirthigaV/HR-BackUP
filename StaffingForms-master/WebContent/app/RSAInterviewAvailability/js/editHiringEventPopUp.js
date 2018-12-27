/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: editHiringEvent.js
 * Application: Staffing Administration
 *
 * Used to edit Hiring event
 * This method gets edit the existing hiring event and,
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
 * Method is used to edit the hiring event and delete.
 * Rename action will be possible for some active hiring
 * events.
 * Validate the components.
 * This method will have hiring calendar summary with details
 * Can able to see the details of hiring slots
 */
function editHiringEvent() {
    /*
     * this will assign each component to a global variable where set values
     * anywhere in this page by using these variables
     */
    var eventName = $("#editEventName");
    var eventDt = $("#editeventDt");
    var eventDtEnd = $("#editeventDtEnd");
    var offSiteEventYes = $("#editOffSiteEventYes");
    var eventStrNum = $("#editEventStrNum");
    var eventAddress = $("#editEventAddress");
    var eventState = $("#editEventState");
    var eventCity = $("#editEventCity");
    var eventZip = $("#editEventZip");
    var venueName = $("#editVenueName");
    var venueAddress = $("#editVenueAddress");
    var venueCity = $("#editVenueCity");
    var venueZip = $("#editVenueZip");
    var eventMgrLdap = $("#editEventMgrLdap");
    var enterStrNum = $("#editEnterStrNum");
    var offSiteEntryYes = $("#editOffSiteEntryYes");
    var offSiteEntryNo = $("#editOffSiteEntryNo");
    /*
     * this function is used to initialize the create hiring event this will
     * empty all the labels initially
     */
    this.initialize = function() {
        /*
         * clear all the divs and label values initially in the popup
         */
        eventDt.val("");
        eventDtEnd.val("");
        eventAddress.empty();
        eventCity.empty();
        eventZip.empty();
        eventState.empty();
        $("#editMgrName").empty();
        $("#editMgrTitle").empty();
        $("#editMgrPhn").empty();
        $("#editMgrMail").empty();
        //restrict the sepecial characters input for input box
        $("input[data-restrict]").restrictInputFeature();
        this.startDateBeginRange = new Date();
        this.endDateBeginRange = new Date();
        this.locationChange = false;
        this.phoneNumberChange = false;
        this.setSelectableRange();
        $("#headerAlertModalLabel").empty();
        CONSTANTS.retailStaffingReq = this;
        CONSTANTS.retailStaffingReq.gridData = [];
        CONSTANTS.YES = "YES";
        CONSTANTS.NO = "NO";
        $(".eventLocDtls").removeClass("opacityDiv");
        $(".partStores").removeClass("opacityDiv");
        $(".opacityDiv input").removeAttr("disabled");
        $(".opacityDiv #editrefreshMgrData").removeAttr("disabled");
        $("#editVenueStateCbo").removeAttr("disabled");
        if($("#editVenueStateCbo").data("selectBox-selectBoxIt")){
        $("#editVenueStateCbo").data("selectBox-selectBoxIt").refresh();
        }
        $( "#editeventDt,#editeventDtEnd" ).datepicker( "option", "disabled", false );
        $(".storeDelBtn").removeAttr("disabled");
        $("#create").removeAttr("disabled");
        $("#editHiringEventPopUp").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        //Draggable for popup
        $("#editHiringEventPopUp").draggable({
            handle: ".modal-header"
         });
        /*
         * this function is called to show the empty grid initially
         */
        CONSTANTS.retailStaffingReq
                .buildStoresGrid(CONSTANTS.retailStaffingReq.gridData);
        MODEL.dirtyPage = false;
        MODEL.closeFlag = false;
        // Clear Hiring Event Manager Data
        CONSTANTS.hiringEventMgrData = null;
        CONSTANTS.interviewScheduleCollection = [];
        CONSTANTS.HiringEventStoreList = [];
        CONSTANTS.editWarningPopupFlag = false;
        // Clear Hiring Event Store Details
        CONSTANTS.hiringEventStoreDetails = [];
        offSiteEntryYes.addClass('hide');
        offSiteEntryNo.removeClass('hide');
        $('#editeventDt, #editeventDtEnd').datepicker({
            beforeShow: this.styleDatePicker.bind(this),
            minDate : 1,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top"

        });
        this.editLocked = false;
        /*
         * this function is called to bind values to the labels and divs which
         * we got from edit service
         */
        CONSTANTS.HiringEventStoreDetailsVO = {};
        CONSTANTS.hiringEventMgrData = MODEL.hiringEventMgrData;
        CONSTANTS.hiringEventStoreDetails
                .push(MODEL.hiringEventDetail.participatingStores);
        CONSTANTS.HiringEventStoreList = MODEL.hiringEventDetail.participatingStores;
        CONSTANTS.HiringEventStoreDetailsVO = CONSTANTS.HiringEventStoreList;
        this.addValue();
        $('select:not(select[multiple])').selectBoxIt();
        $("#editEventStrNum").off('paste');
        $("#editEventStrNum").on('paste', function(e) {
            setTimeout(function(){
                var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));               
            },100);
        });
        $("#editEnterStrNum").off('paste');
        $("#editEnterStrNum").on('paste', function(e) {
            setTimeout(function(){
                var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));               
            },100);
        });
        $("#editEventStrNum").attr("title",CONSTANTS.CREATE_HIRING_EVENT_STORE_NUMBER_TOOLTIP);
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
         $("#editEventName").off('paste');
         $("#editEventName").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
     	 * Copy and paste the selected event manager Ldap text
     	 * It allows some special characters with alphabets
     	 */
         $("#editEventMgrLdap").off('paste');
         $("#editEventMgrLdap").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9a-zA-ZÀ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
     	 * Copy and paste the selected event venue name text
     	 * It allows some special characters with alphabets
     	 */
         $("#editVenueName").off('paste');
         $("#editVenueName").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
     	 * Copy and paste the selected event venue address text
     	 * It allows some special characters with alphabets
     	 */
         $("#editVenueAddress").off('paste');
         $("#editVenueAddress").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
      	 * Copy and paste the selected event venue city text
      	 * It allows some special characters with alphabets
      	 */
         $("#editVenueCity").off('paste');
         $("#editVenueCity").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
      	 * Copy and paste the selected event venue zip text
      	 * It allows some special characters with alphabets
      	 */
         $("#editVenueZip").off('paste');
         $("#editVenueZip").on('paste', function(e) {
             setTimeout(function(){
                  var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                  $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
             },100);
         });
    };
    /*
     * this function is used to set flag as true if user changes any fields in
     * edit hiring event pop up
     */
    this.setDirtyPage = function() {
        MODEL.dirtyPage = true;
    };
    this.styleDatePicker = function() {
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    /*
     * this function is used to populate the value on all the fields from where
     * we got from the edit hiring event detail service
     */
    this.addValue = function() {
        if (MODEL.hiringEventDetail) {
            $("#editEventName").val(MODEL.hiringEventDetail.eventName);
            //set the event date
            MODEL.hiringEventDetail.eventDate = MODEL.hiringEventDetail.eventDate
                    .substr(5, 2)+ "/"
                    + MODEL.hiringEventDetail.eventDate.substr(8, 2)
                    + "/" + MODEL.hiringEventDetail.eventDate.substr(0, 4);
            //set the date end evnt
            MODEL.hiringEventDetail.eventDateEnd = MODEL.hiringEventDetail.eventDateEnd
                    .substr(5, 2)+ "/"
                    + MODEL.hiringEventDetail.eventDateEnd.substr(8, 2)
                    + "/" + MODEL.hiringEventDetail.eventDateEnd.substr(0, 4);
            $("#editeventDt").val(MODEL.hiringEventDetail.eventDate);
            $("#editeventDtEnd").val(MODEL.hiringEventDetail.eventDateEnd);
            //validate the radio button has "Y" or "N"
            if (MODEL.hiringEventDetail.offSiteEvent === "Y") {
                $("#editOffSiteEventYes").prop('checked', true);
                $("#editVenueName").val(MODEL.hiringEventDetail.venueStoreName);
                $("#editVenueAddress").val(
                        MODEL.hiringEventDetail.eventAddress1);
                $("#editVenueCity").val(MODEL.hiringEventDetail.eventCity);
                $("#editVenueZip").val(MODEL.hiringEventDetail.eventZip);
            } else if (MODEL.hiringEventDetail.offSiteEvent === "N") {
                $("#editOffSiteEventNo").prop('checked', true);
                // Need to fetch the store number. as of now, service is not returning the store number.
                $("#editEventStrNum").val("");
                $("#editEventAddress").html(
                        MODEL.hiringEventDetail.eventAddress1);
                $("#editEventCity").html(MODEL.hiringEventDetail.eventCity);
                $("#editEventState").html(MODEL.hiringEventDetail.eventState);
                $("#editEventZip").html(MODEL.hiringEventDetail.eventZip);
            }
            this.onSite_OffSiteShow(false);
            //Check the hiringEventDetail has participatingStores
            if (MODEL.hiringEventDetail.participatingStores) {
                CONSTANTS.retailStaffingReq
                        .buildStoresGrid(MODEL.hiringEventDetail.participatingStores);
            }
        }
        /*
         * If hiringEventMgrData object has data then
         * set the value for html elements
         */
        if (MODEL.hiringEventMgrData) {
            $("#editMgrName").html(MODEL.hiringEventMgrData.eventMgrName);
            $("#editMgrTitle").html(MODEL.hiringEventMgrData.eventMgrTitle);
            $("#editMgrPhn").html(MODEL.hiringEventMgrData.eventMgrPhone);
            $("#editMgrMail").html(MODEL.hiringEventMgrData.eventMgrEmail);
        }
        /*
         * this function is used to add the buttons to event start date and end
         * date
         */
        $('#editEventDt, #editEventDtEnd').datepicker({
            beforeShow: this.styleDatePicker.bind(this),
            minDate : 1,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top"
        });
        //get the current time
        var currentDateTime = new Date();
        //format the time
        var hiringEventBeginDateTime = new Date(
                parseInt(MODEL.hiringEventDetail.eventDate.substr(6)),
                parseInt(MODEL.hiringEventDetail.eventDate.substr(0, 2)) - 1,
                parseInt(MODEL.hiringEventDetail.eventDate.substr(3, 2)), 0, 0,
                0, 0);
        var milliSeconds = hiringEventBeginDateTime.getTime()
                - currentDateTime.getTime();
        //validate the milliseconds
        if ((milliSeconds / 1000) / 3600 < 12) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            this.editLocked = true;
            $("#headerAlertModalLabel").text("Edit Hiring Event");
            $("#editAlertPopupModal .alertModalBody")
                    .html("Changes to this Hiring Event are not " +
                            "\nallowed.  Changes are only allowed before 12:00pm the day before" +
                            "\nthe Event Start.");
            editHiringEvent.addEditModalCss();
            $(".eventLocDtls").addClass("opacityDiv");
            $(".partStores").addClass("opacityDiv");
            $(".opacityDiv input").attr("disabled", "disabled");
            $(".opacityDiv #editrefreshMgrData").attr("disabled", "disabled");
             $("#editVenueStateCbo").attr("disabled", "disabled");
            if($("#editVenueStateCbo").data("selectBox-selectBoxIt")){
                $("#editVenueStateCbo").data("selectBox-selectBoxIt").refresh();
                }
            $( "#editeventDt,#editeventDtEnd" ).datepicker( "option", "disabled", true );
            $(".storeDelBtn").attr("disabled", "disabled");
            $("#create").attr("disabled","disabled");
        } else if (MODEL.interviewsScheduled) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
             $("#headerAlertModalLabel").text("Edit Hiring Event");
            $("#editAlertPopupModal .alertModalBody").html(
                            "Interviews have been scheduled for this Hiring Event, therefore the Event Dates cannot be changed.");
            editHiringEvent.addEditModalCss();
            $("#editeventDt").datepicker( "option", "disabled", true );
            $("#editeventDtEnd").datepicker( "option", "disabled", true );
        }
    };
    /*
     * this function is called when we select yes for offsite event it is used
     * to append the values to state drop down
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray,
            textKey, valueKey) {
        if (action === "append") {
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
            }
            $('#' + drpDownId).attr('disabled', false);
            if ($('#' + drpDownId).data("selectBox-selectBoxIt")) {
                $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
            }
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
        }
    };
    /*
     * this method is used to focus the event start date
     */
    this.focusFromDate = function() {
        $('#editEventDt').focus();
    };
    /*
     * this method is used to focus the event end date
     */
    this.focusToDate = function() {
        $('#editEventDtEnd').focus();
    };
    /*
     * this function is used to add the stores to the hiring event
     */
    this.addStoreToHiringEvent = function() {
        var storeExists = false;
        var showPopErrorMessage = true;
        try {
            // if it is triggered from edit event store number, do not show the popup message.
            showPopErrorMessage = !(event.srcElement.id.toString() === "editEventStrNum");
        } catch(ex) {}

        if (enterStrNum.val().length === 4) {
            /*
             * Make sure that the requested Store is not already in the list
             */
            for ( var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
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
                this.setDirtyPage();
                var callbackFunction = $.Callbacks('once');
                this.addHiringEventStoresURL = CONSTANTS.addHiringEventStoresURL;
                callbackFunction.add(this.addHiringEventStoresResult);
                RSASERVICES.addHiringEventStores(callbackFunction,
                        this.addHiringEventStoresURL, enterStrNum.val(), true,
                        "Please wait...");
            } else if(showPopErrorMessage) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#editAlertPopupModal .alertModalBody").html(
                        "Store Number:" + enterStrNum.val()
                                + " already exists in the list.");
                editHiringEvent.addEditModalCss();
            }
            enterStrNum.val("");
        }
    };
    /*
     * this function is called based on the event is offsite or onsite
     */
    this.onSite_OffSiteShow = function(clear) {
        if ($("input[name=offsiteEvent]:checked").attr("id") === "editOffSiteEventYes") {
            CONSTANTS.offSiteEventFlg = "YES";
        } else {
            CONSTANTS.offSiteEventFlg = "NO";
        }
        $("#editVenueStateCbo").empty();
        this.appendOptionToSelect('editVenueStateCbo', 'append',
                CONSTANTS.reqStateDet, 'stateName', 'stateCode');
        $(
                '#editVenueStateCbo option[value="'
                        + MODEL.hiringEventDetail.eventState + '"]').attr(
                "selected", "selected");
        if ((MODEL.hiringEventDetail.offSiteEvent !== CONSTANTS.offSiteEventFlg)
                && CONSTANTS.offSiteEventFlg === "YES") {
            // Update all Stores eventSiteFlg to False.
            for ( var i = 0; i < MODEL.hiringEventDetail.participatingStores.length; i++) {
                CONSTANTS.HiringEventStoreDetailsVO[i].eventSiteFlg = "N";
            }
        }
        /*
         * if it is offsitevent show the corresponding divs
         */
        if (offSiteEventYes.prop('checked')) {

            offSiteEntryYes.removeClass('hide');
            offSiteEntryNo.addClass('hide');
            eventStrNum.val("");
            eventAddress.text("");
            eventCity.text("");
            eventZip.text("");
            eventState.text("");
        }
        /*
         * if it is a onsite event show the corresponsing divs
         */
        else {
            offSiteEntryYes.addClass('hide');
            offSiteEntryNo.removeClass('hide');
        }
        if (clear) {
            MODEL.hiringEventDetail.venueStoreName = "";
            MODEL.hiringEventDetail.eventAddress1 = "";
            MODEL.hiringEventDetail.eventCity = "";
            MODEL.hiringEventDetail.eventState = "";
            MODEL.hiringEventDetail.eventZip = "";
            MODEL.hiringEventDetail.strNumber = "";
            enterStrNum.val("");
            venueName.val("");
            venueAddress.val("");
            venueCity.val("");
            venueZip.val("");
            $('#editVenueStateCbo option:first-child').attr("selected",
                    "selected");
        }
    };
    /*
     * this function is used to set the location value in hiring event details
     * vo
     */
    this.setLocationValue = function(finalCheckVal, locationChange,
            phoneNumberChange) {
        var checkVal = finalCheckVal || locationChange || phoneNumberChange;
        if (checkVal) {
            return checkVal;
        } else {
            return !checkVal;
        }
    };
    /*
     * this function is used to frame participating store object it will assign
     * the value eventSiteFlg as Y or N based on a condition
     */
    this.setParticipatingStores = function() {
        var tempArray = [];
        for ( var editStoreList = 0; editStoreList < CONSTANTS.HiringEventStoreList.length; editStoreList++) {
            var tempStores = {};
            tempStores.number = CONSTANTS.HiringEventStoreList[editStoreList].number;
            /*
             * eventSiteFlg is Y if it is the created store if it is not a
             * created store and only it is a participating store set as 'N'
             */
            if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
                if (tempStores.number === eventStrNum.val()) {
                    tempStores.eventSiteFlg = "Y";
                } else {
                    tempStores.eventSiteFlg = "N";
                }
            } else {
                tempStores.eventSiteFlg = "N";
            }
            /*
             * if the store is already present add as update else newly added
             * store as update
             */
            if (MODEL.hiringEventDetail.participatingStores[editStoreList].hireEventId === undefined
                    || MODEL.hiringEventDetail.participatingStores[editStoreList].hireEventId === 0) {
                tempStores.hireEventId = MODEL.hiringEventDetail.hireEventId;
                tempStores.updateInsertRecord = "INSERT";
            } else {
                tempStores.hireEventId = MODEL.hiringEventDetail.participatingStores[editStoreList].hireEventId;
                tempStores.updateInsertRecord = "UPDATE";
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
        // Determine the Hiring Event Type
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
     * this function is used to set location change value based on interview
     * scheduled
     */
    this.isInterviewScheduled = function(venueStoreNametemp, eventAddress1temp,
            eventCitytemp, eventStatetemp, eventZiptemp) {
        if (MODEL.interviewsScheduled) {
            var checkVenueNameAddress = venueStoreNametemp !== MODEL.hiringEventDetail.venueStoreName
                    || eventAddress1temp !== MODEL.hiringEventDetail.eventAddress1
                    || eventCitytemp !== MODEL.hiringEventDetail.eventCity;
            var checkState = eventStatetemp !== MODEL.hiringEventDetail.eventState
                    || eventZiptemp !== MODEL.hiringEventDetail.eventZip;
            var finalCheckVal = checkVenueNameAddress || checkState;
            var val = this.setLocationValue(finalCheckVal, this.locationChange,
                    this.phoneNumberChange);
            return val;
        } else {
            return "";
        }
    };
    /*
     * this function is used to frame the request t send for create hiring event
     * service
     */
    this.validateUpdateHiringEvent = function() {
        $('#editwarningpopup .modal-body').removeClass('addHeight');
        var hiringEventDetailsVO = {};
        hiringEventDetailsVO.hireEventName = eventName.val();
        hiringEventDetailsVO.eventDate = this.convertDate(eventDt.val());
        hiringEventDetailsVO.eventDateEnd = this.convertDate(eventDtEnd.val());
        hiringEventDetailsVO.hireEventId = MODEL.hiringEventDetail.hireEventId;
        hiringEventDetailsVO.reqnCalId = MODEL.hiringEventDetail.reqnCalId;
        hiringEventDetailsVO.offSiteEvent = CONSTANTS.offSiteEventFlg;
        hiringEventDetailsVO.hireEventCreatedByStore = MODEL.hiringEventDetail.hireEventCreatedByStore;
        //validate the radio button is yes
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
            //Assign all the input values to hiringEventDetailsVO object
            hiringEventDetailsVO.strNumber = eventStrNum.val();
            hiringEventDetailsVO.venueStoreName = MODEL.hiringEventDetail.venueStoreName;
            hiringEventDetailsVO.eventAddress1 = eventAddress.text();
            hiringEventDetailsVO.eventCity = eventCity.text();
            hiringEventDetailsVO.eventState = eventState.text();
            hiringEventDetailsVO.eventZip = eventZip.text();
        }
        else {
            hiringEventDetailsVO.strNumber = "";
            hiringEventDetailsVO.venueStoreName = venueName.val();
            hiringEventDetailsVO.eventAddress1 = venueAddress.val();
            hiringEventDetailsVO.eventCity = venueCity.val();
            hiringEventDetailsVO.eventState = $(
                    '#editVenueStateCbo option:selected').val();
            hiringEventDetailsVO.eventZip = venueZip.val();
        }
        /*
         * validate the method having address abd city,state
         */
        hiringEventDetailsVO.locationChange = this.isInterviewScheduled(
                hiringEventDetailsVO.venueStoreName,
                hiringEventDetailsVO.eventAddress1,
                hiringEventDetailsVO.eventCity,
                hiringEventDetailsVO.eventState, hiringEventDetailsVO.eventZip);
        //Assign those values to hiringEventDetailsVO object
        hiringEventDetailsVO.eventMgrAssociateId = CONSTANTS.hiringEventMgrData.eventMgrAssociateId;
        hiringEventDetailsVO.eventMgrPhone = CONSTANTS.hiringEventMgrData.eventMgrPhone;
        hiringEventDetailsVO.eventMgrName = CONSTANTS.hiringEventMgrData.eventMgrName;
        hiringEventDetailsVO.eventMgrTitle = CONSTANTS.hiringEventMgrData.eventMgrTitle;
        hiringEventDetailsVO.eventMgrEmail = CONSTANTS.hiringEventMgrData.eventMgrEmail;
        hiringEventDetailsVO.eventMgrId = CONSTANTS.hiringEventMgrData.eventMgrId;
        // Set the participating stores in the VO
        hiringEventDetailsVO.participatingStores = this
                .setParticipatingStores();

        hiringEventDetailsVO.hiringEventType = this
                .setEventType(hiringEventDetailsVO.participatingStores);
        CONSTANTS.HiringEventDetailsVO = hiringEventDetailsVO;
        hiringEventDetailsVO.modifyAvailabilityList = this
                .setModifyAvaliabilityList();
        this.availabilityMessage = "";
        this.setAvailabilityMessage(this.eventStartDateChanged,
                this.eventEndDateChanged, this.datesChanged,
                this.numberOfDaysInOriginalEvent, this.numberOfDaysInNewEvent);
        CONSTANTS.HiringEventDetailsVO = hiringEventDetailsVO;
        $("#editwarningpopup #WarningModalLabel").text("Warning");
        $(".cautionMsg")
                .html(
                        "You are about to Update this Hiring Event.  Please notify your RSM of any changes.");
        $(".sureMsg").html("Are You Sure?");
        $("#editwarningpopup #oldRenameCalName").html(this.availabilityMessage);
        this.setDraggableForWarning();
        $("#editwarningpopup").modal('show');
        $("#editwarningpopup .modal-dialog").addClass("editWarningWidth");
    };
    /*
     * this function is used to check whether event dats are changed or not
     */
    this.checkDates = function() {
        var returnEvent=false;
        if (this.eventStartDateChanged || this.eventEndDateChanged) {
            returnEvent = true;
        }
        return returnEvent;
    };
    /*
     * Method is used to assign modify avaliability list to hiring event
     * detail VO objects
     * It will validat the start and end event dates with time.
     * Returns the date changed after calculation.
     */
    this.setModifyAvaliabilityList = function() {
        /*
         * Get Original start event date using the substring from hiringEventDetail
         * object
         */
        var formattedOrginalEventDate = MODEL.hiringEventDetail.orginalEventDate
                .substr(5, 2)+ "/"
                + MODEL.hiringEventDetail.orginalEventDate.substr(8, 2)
                + "/" + MODEL.hiringEventDetail.orginalEventDate.substr(0, 4);
        /*
         * Get Original end event date using the substring from hiringEventDetail
         * object
         */
        var formattedOrginalEventDateEnd = MODEL.hiringEventDetail.orginalEventDateEnd
                .substr(5, 2)+ "/"
                + MODEL.hiringEventDetail.orginalEventDateEnd.substr(8, 2)+ "/"
                + MODEL.hiringEventDetail.orginalEventDateEnd.substr(0, 4);
        /*
         * Get Original start date time using the substring from hiringEventDetail
         * object
         */
        var hiringEventOriginalStartDateTime = new Date(
                parseInt(formattedOrginalEventDate.substr(6)),
                parseInt(formattedOrginalEventDate.substr(0, 2)) - 1,
                parseInt(formattedOrginalEventDate.substr(3, 2)),0, 0, 0, 0);
        /*
         * Get Original end event date using the substring from hiringEventDetail
         * object
         */
        var hiringEventOriginalEndDateTime = new Date(
                parseInt(formattedOrginalEventDateEnd.substr(6)),
                parseInt(formattedOrginalEventDateEnd.substr(0,2)) - 1,
                parseInt(formattedOrginalEventDateEnd.substr(3,
                        2)), 0, 0, 0, 0);
        /*
         * Calculation for time start and end
         */
        var hiringEventStartDateTime = new Date(parseInt(eventDt.val()
                .substr(6)), parseInt(eventDt.val().substr(0, 2)) - 1,
                parseInt(eventDt.val().substr(3, 2)), 0, 0, 0, 0);
        var hiringEventEndDateTime = new Date(parseInt(eventDtEnd.val().substr(
                6)), parseInt(eventDtEnd.val().substr(0, 2)) - 1,
                parseInt(eventDtEnd.val().substr(3, 2)), 0, 0, 0, 0);
        var millisecondsInOriginalEventDuration = hiringEventOriginalEndDateTime
                .getTime()
                - hiringEventOriginalStartDateTime.getTime();
        var millisecondsInNewEventDuration = hiringEventEndDateTime.getTime()
                - hiringEventStartDateTime.getTime();
        var millisecondsBetweenOriginalEventStartDateAndNew = hiringEventStartDateTime
                .getTime()
                - hiringEventOriginalStartDateTime.getTime();
        var numberOfDaysInOriginalEvent = (Math
                .abs(millisecondsInOriginalEventDuration
                        / CONSTANTS.MILLISECONDS_PER_DAY) + 1);
        this.numberOfDaysInOriginalEvent = numberOfDaysInOriginalEvent;
        var numberOfDaysInNewEvent = (Math.abs(millisecondsInNewEventDuration
                / CONSTANTS.MILLISECONDS_PER_DAY) + 1);
        this.numberOfDaysInNewEvent = numberOfDaysInNewEvent;
        var numberOfDaysBetweenOriginalDateAndNewEventDate = ((millisecondsBetweenOriginalEventStartDateAndNew / CONSTANTS.MILLISECONDS_PER_DAY));
        this.eventEndDateChanged = false;
        this.eventEndDateChanged = (hiringEventOriginalEndDateTime.getTime() !== hiringEventEndDateTime
                .getTime()) ? true : false;
        this.eventStartDateChanged = (hiringEventOriginalStartDateTime
                .getTime() !== hiringEventStartDateTime.getTime()) ? true
                : false;
        var datesChanged = [];
        //validate the dates
        this.setEventStartOrEnd = this.checkDates();
        //check the setEventStartOrEnd flag has true or false
        if (this.setEventStartOrEnd) {
            for ( var i = 0; i < numberOfDaysInOriginalEvent; i++) {
                var hiringEventModifyAvailabilityVO = {};
                var tempDate = new Date();
                tempDate.setTime(hiringEventOriginalStartDateTime.getTime()
                        + (i * CONSTANTS.MILLISECONDS_PER_DAY));
                var year = tempDate.getFullYear();
                var month = (1 + tempDate.getMonth()).toString();
                month = month.length > 1 ? month : '0' + month;
                var day = tempDate.getDate().toString();
                day = day.length > 1 ? day : '0' + day;
                tempDate = year + "-" + month + "-" + day;
                /*
                 * create the hiringEventModifyAvailabilityVO object and add
                 * the values in to this.
                 */
                hiringEventModifyAvailabilityVO.theDate = tempDate;
                hiringEventModifyAvailabilityVO.moveDeleteAvailability = (numberOfDaysInOriginalEvent === 1 && numberOfDaysInNewEvent === 1) ? "MOVE"
                        : "DELETE";
                hiringEventModifyAvailabilityVO.daysBetweenOriginalStartDateAndNewStartDate = numberOfDaysBetweenOriginalDateAndNewEventDate;
                hiringEventModifyAvailabilityVO.reqnCalId = MODEL.hiringEventDetail.reqnCalId;
                datesChanged.push(hiringEventModifyAvailabilityVO);
            }
            this.datesChanged = datesChanged;
            return datesChanged;
        }
    };
    /*
     * this function is used to set the availabilty message
     */
    this.setAvailabilityMessage = function(eventStartDateChanged,
            eventEndDateChanged, datesChangedArray,
            numberOfDaysInOriginalEvents, numberOfDaysInNewEvents) {
        /*
         * if event date is changed then show prompt accordingly
         */
        if (eventStartDateChanged || eventEndDateChanged) {
            //check the length of changed date
            if (datesChangedArray.length === 1
                    && numberOfDaysInOriginalEvents === 1
                    && numberOfDaysInNewEvents === 1) {
                this.availabilityMessage = "  All Availability for "
                        + this.convertEditDate(datesChangedArray[0].theDate)
                        + " will be moved to " + eventDt.val() + ".";
            } else {
                this.availabilityMessage = "  Due to Event Date Changes, all original Availability for will be deleted. " +
                        " New Availability will need to be added for the Event.";
                $('#editwarningpopup .modal-body').addClass('addHeight');
            }
        }
    };
    /*
     * this function is sued to set the offsite event value based on the
     * selected offsite/onsite
     */
    this.setOffsiteEventFlag = function() {
        if ($("input[name=offsiteEvent]:checked").attr("id") === "editOffSiteEventYes") {
            CONSTANTS.offSiteEventFlg = "YES";
        } else {
            CONSTANTS.offSiteEventFlg = "NO";
        }
    };
    /*
     * this function is used validate the event start date and end date
     */
    this.checkNameDate = function() {
        /*
         * check for the event name if it is null show the prompt to enter event
         * name
         */
        if (eventName.val() === "" || eventName.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Event Name is a required field.");
            eventName.focus();
            return true;
        }
        /*
         * check for event date if it is null show the prompt to select the date
         */
        if (eventDt.val() === "" || eventDtEnd.val() === "") {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Event Date is a required field.");
            return true;
        }
        return false;
    };
    /*
     * this function is used to validate the venuecity,venuestate,venue name,
     * eventstore number
     */
    this.checkOffsiteEventFlag = function() {
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.YES) {
            var checkCityState = true;
            /*
             * checks for the venue name if it is null prompt to eneter the
             * venue name
             */
            if (venueName.val() === "" || venueName.val().trim().length <= 0) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#editAlertPopupModal .alertModalBody").html(
                        "Venue Name is a required field.");
                venueName.focus();
                return true;
            }
            /*
             * check venues will check the city,state,address and zip venues are
             * null or not
             */
            checkCityState = this.checkCityStateZip();
            if (checkCityState) {
                return true;
            }
        } else {
            /*
             * check for store number is valid
             */
            if (eventStrNum.val() === "" || eventStrNum.val().length < 4) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#editAlertPopupModal .alertModalBody").html(
                        "Store Number is required for an On-Site Event");
                $("#editAlertPopupModal .modal-content").addClass(
                        "editHiringEventlargeModal");
                $("#editAlertPopupModal .alertModalBody").addClass(
                        "editHiringEventalignModal");
                $('#editAlertPopupModal').on('hidden.bs.modal', function() {
                    editHiringEvent.removeEditAlertCss();
                });
                eventStrNum.focus();
                return true;
            }
        }
        return false;
    };
    /*
     * this function is sued to validate venue city,venue address,venue state
     */
    this.checkCityStateZip = function() {
        /*
         * checks for the venue address if it is null prompt to eneter the venue
         * address
         */
        if (venueAddress.val() === "" || venueAddress.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Venue Address is a required field.");
            venueAddress.focus();
            return true;
        }
        /*
         * check for venue city if it is null show the prompt to provide the
         * venue city
         */
        if (venueCity.val() === "" || venueCity.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Venue City is a required field.");
            venueCity.focus();
            return true;
        }
        /*
         * check for event state.If none of the state selected then prompt to
         * select the state from the dropdown
         */
        if ($("#editVenueStateCbo option:selected").index() === -1) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Venue State is a required field.");
            $("#editVenueStateCbo").focus();
            return true;
        }
        /*
         * check for event venue zip if it is null show the prompt toenter
         * postal code
         */
        if (venueZip.val() === "" || venueZip.val().trim().length <= 0) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Venue Postal Code is a required field.");
            venueName.focus();
            editHiringEvent.addEditModalCss();
            $("#editAlertPopupModal .alertModalBody").removeClass(
                    "editHiringEventalignModal");
            $("#editAlertPopupModal .modal-body ").removeClass(
                    "editHiringEventlargeBody");
            return true;
        }
        return false;
    };
    /*
     * this function is used for validation while creating the hiring event this
     * will check the manager details and store list
     */
    this.checkManagerStoreList = function() {
        /*
         * if hiringEventMgrData is null show the prompt Manager data required
         */
        if (!CONSTANTS.hiringEventMgrData
                || !CONSTANTS.hiringEventMgrData.eventMgrName) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(
                            "Please enter a LDAP ID and click the Refresh Button.  Event Manager Data Required.");
            editHiringEvent.addEditModalCss();
            return true;
        }
        /*
         * if HiringEventStoreList is null show the promptatleast one
         * participating store
         */
        if (CONSTANTS.HiringEventStoreList.length < 1) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "There must be at least one Participating Store");
            editHiringEvent.addEditModalCss();
            enterStrNum.focus();
            return true;
        }
        return false;
    };
    /*
     * this function will call numerous function to validate all the fields
     * which in turn call update hiring event
     */
    this.updateHiringEvent = function() {
        /*
         * Make sure that required fields are populated set offsite event flag
         * based on selected radio button by calling function checkEventDt
         */
        this.setOffsiteEventFlag();
        var cont = true;
        cont = this.checkNameDate();
        if (cont) {
            return;
        }
        cont = true;
        cont = this.checkOffsiteEventFlag();
        if (cont) {
            return;
        }
        /*
         * check store list
         */
        cont = true;
        cont = this.checkManagerStoreList();
        if (cont) {
            return;
        }
        /*
         * validate the fields
         */
        cont = true;
        cont = this.checkHostStrInList();
        if (cont) {
            return;
        }
        /*
         * form the hiring event details vo
         */
        this.validateUpdateHiringEvent();
        // Set the Event Data in a VO
    };
    /*
     * this function is used for validation while creating the hiring event this
     * will check the store is being held in the participating store or not
     */
    this.checkHostStrInList = function() {
        var hostStrInList = false;
        // Make sure that the Host Store is in the Participating Store List
        if (CONSTANTS.offSiteEventFlg === CONSTANTS.NO) {
            for ( var m = 0; m < CONSTANTS.HiringEventStoreList.length; m++) {
                if (CONSTANTS.HiringEventStoreList[m].number.toString() === eventStrNum
                        .val().toString()) {
                    hostStrInList = true;
                }
            }
            /*
             * The store number created should present in participating stores,
             * else show the prompt
             */
            if (!hostStrInList) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#editAlertPopupModal .alertModalBody")
                        .html(
                                "The Store that the Event is being held at must be in the Participating Stores List.");
                editHiringEvent.addEditModalCss();
                $("#editAlertPopupModal .alertModalBody").removeClass(
                        "editHiringEventalignModal");
                return true;
            }
        }
        return false;
    };
    /*
     * Method is called when yes is clicked for edit warning pop up
     */
    this.yesClicked = function() {
        if (MODEL.closeFlag) {
            $("#editwarningpopup").modal('hide');
            $("#editHiringEventPopUp").modal('hide');
            hiringEvent.loadData();
        }
        /*
         * If CONSTANTS.editWarningPopupFlag is false then the service
         * calls happens.
         * The parameters will be event name and id
         */
        else {
            if (!CONSTANTS.editWarningPopupFlag) {
                var callbackFunction = $.Callbacks('once');
                this.checkDuplicateHiringEventNameForeditURL = CONSTANTS.checkDuplicateHiringEventNameForeditURL;
                callbackFunction.add(this.chkDuplicateHiringEvntNameResult);
                RSASERVICES.checkDuplicateHiringEventNameForedit(
                        callbackFunction,
                        this.checkDuplicateHiringEventNameForeditURL,
                        CONSTANTS.HiringEventDetailsVO.hireEventName,
                        CONSTANTS.HiringEventDetailsVO.hireEventId);
            } else {
                CONSTANTS.editWarningPopupFlag = false;
                this.warnYesClicked();
            }
        }
    };
    /*
     * Method is used to add the store list in to the grid
     * for populating the data.
     */
    this.addHiringEventStoresResult = function(data) {
        var returnedErrorMessage = "";
        var hiringEventStoreDetailsVO = {};
        enterStrNum.val("");
        /*
         * Iterate the HiringEventStoreList array and splice if
         * number is empty
         */
        for ( var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
            if (CONSTANTS.HiringEventStoreList[i].number.toString() === "") {
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
                    //Add the response values into hiringEventStoreDetailsVO object
                    hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                    hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
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
                $("#editAlertPopupModal .alertModalBody").html(
                        "Store/Location Number " + enterStrNum.val()
                                + " is invalid.");
                editHiringEvent.addEditModalCss();
                $("#editAlertPopupModal .modal-body ").removeClass(
                        "editHiringEventlargeBody");
                $("#editAlertPopupModal .alertModalBody").removeClass(
                        "editHiringEventalignModal");
            }
        }
        /*
         * check for return errors
         */
        else if (data.storeResponse.hasOwnProperty("error")) {
            if (_.isArray(data.storeResponse.error)) {
                returnedErrorMessage = data.storeResponse.error.errorMsg;
            } else {
                returnedErrorMessage = [ data.storeResponse.error.errorMsg ];
            }
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(returnedErrorMessage);
        }
    };
    /*
     * this function is used to build the grid
     */
    this.buildStoresGrid = function(response) {
        CONSTANTS.retailStaffingReq.gridData = [];
        CONSTANTS.retailStaffingReq.gridData = response;
        var grid;
        var columns = [
        /*
         * this column is used to show the store number
         */
        {
            id : "number",
            sortable : false,
            name : "Store #",
            field : "number",
            width : 50
        },
        /*
         * this column is used to show the store name
         */
        {
            id : "name",
            sortable : false,
            name : "Store Name",
            field : "name",
            width : 50
        },
        /*
         * this column is used to show the delete button
         */
        {
            id : "delete",
            sortable : false,
            name : "Delete Store",
            field : "name",
            formatter : this.deleteFormatter,
            width : 50
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
        /*
         * if temp data is not equal to array then
         * the tempdata will pushed in to an data array
         */
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 3) {
            var emptyData = [];
            for ( var i = 0; i < 2; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        grid = new Slick.Grid("#storesEditgrid", data, columns, options);
        //window resize function call for container can be resized
        $(window).resize(function() {
            $("#storesEditgrid").width($(".gridContainer").width());
            $(".slick-viewport").width($("#myGrid").width());
            grid.resizeCanvas();
        });
        /*
         * on click of delete hiring buttin the tempdata
         * asigned to selectedStoreSummary array and it
         * has method called upon based.
         */
        $(".storeDelBtn").on("click", function(e) {
            var rowId = $(this).attr("id").split("_");
            var tempdata = grid.getDataItem(rowId[1]);
            CONSTANTS.selectedStoreSummary = tempdata;
            CONSTANTS.retailStaffingReq.deleteStoreFromList(e);
        });
    };
    /*
     * Method is used to add a delete button in a row of the grid
     */
    this.deleteFormatter = function(row, cell, value) {
        var deleteTooltip="";
        if(CONSTANTS.retailStaffingReq.gridData[row].activeReqCount &&
                parseInt(CONSTANTS.retailStaffingReq.gridData[row].activeReqCount) > 0){
            deleteTooltip = CONSTANTS.retailStaffingReq.gridData[row].activeReqCount.toString() +' Active Requisition(s)';
        }
        if(CONSTANTS.retailStaffingReq.gridData[row].hasOwnProperty("storeDeleteAllowed") && !CONSTANTS.retailStaffingReq.gridData[row].storeDeleteAllowed) {
            return value ? ('<div title="'+deleteTooltip+'"><input type="button" id="delBtn_' + row + '" class="storeDelBtn" value="Delete" disabled="disabled"></div>') : '';
        }
        else {
            return value ? ('<div title="'+deleteTooltip+'"><input type="button" id="delBtn_' + row + '" class="storeDelBtn" value="Delete"></div>'): '';
        }
    };
    /*
     * this function is the success handler of check duplicate hiring event
     * service this will frame a request to send cretae hiring event service
     * call
     */
    this.chkDuplicateHiringEvntNameResult = function(data) {
        CONSTANTS.HiringEventDetailsVO.stores = [];
        /*
         * form request to edit hiring event
         */
        if (data.Response.hasOwnProperty("status")) {
            for ( var i = 0; i < CONSTANTS.HiringEventDetailsVO.participatingStores.length; i++) {
                CONSTANTS.HiringEventDetailsVO.stores
                        .push({
                            "number" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].number,
                            "eventSiteFlg" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].eventSiteFlg,
                            "hireEventId" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].hireEventId,
                            "updateInsertRecord" : CONSTANTS.HiringEventDetailsVO.participatingStores[i].updateInsertRecord
                        });
            }
            /*
             * Call the Hiring Event service
             */
            var updateHiringRequest = {
                "UpdateHiringEventRequest" : {
                    "hiringEventDetail" : {
                        "hireEventId" : CONSTANTS.HiringEventDetailsVO.hireEventId,
                        "reqnCalId" : CONSTANTS.HiringEventDetailsVO.reqnCalId,
                        "hiringEventType" : CONSTANTS.HiringEventDetailsVO.hiringEventType,
                        "strNumber" : MODEL.StoreNumber,
                        "eventName" : CONSTANTS.HiringEventDetailsVO.hireEventName,
                        "eventDt" : CONSTANTS.HiringEventDetailsVO.eventDate,
                        "eventDtEnd" : CONSTANTS.HiringEventDetailsVO.eventDateEnd,
                        "eventMgrName" : CONSTANTS.HiringEventDetailsVO.eventMgrName,
                        "eventMgrId" : CONSTANTS.HiringEventDetailsVO.eventMgrId,
                        "eventMgrAssociateId" : CONSTANTS.HiringEventDetailsVO.eventMgrAssociateId,
                        "eventMgrTitle" : CONSTANTS.HiringEventDetailsVO.eventMgrTitle,
                        "eventMgrPhone" : CONSTANTS.HiringEventDetailsVO.eventMgrPhone,
                        "eventMgrEmail" : CONSTANTS.HiringEventDetailsVO.eventMgrEmail,
                        "venueStoreName" : CONSTANTS.HiringEventDetailsVO.venueStoreName,
                        "eventAddress1" : CONSTANTS.HiringEventDetailsVO.eventAddress1,
                        "eventCity" : CONSTANTS.HiringEventDetailsVO.eventCity,
                        "eventState" : CONSTANTS.HiringEventDetailsVO.eventState,
                        "eventZip" : CONSTANTS.HiringEventDetailsVO.eventZip,
                        "hireEventCreatedByStore" : CONSTANTS.HiringEventDetailsVO.hireEventCreatedByStore,
                        "locationChange" : CONSTANTS.HiringEventDetailsVO.locationChange
                    },
                    "participatingStores" : {
                        "store" : CONSTANTS.HiringEventDetailsVO.stores
                    }
                }
            };
            /*
             * If HiringEventDetailsVO object has modifyAvailabilityList then
             * the request will form into this.
             */
            if (CONSTANTS.HiringEventDetailsVO.modifyAvailabilityList) {
                updateHiringRequest.UpdateHiringEventRequest.availabilityModifications = {
                        "modification": {
                            "theDate": CONSTANTS.HiringEventDetailsVO.modifyAvailabilityList.theDate,
                            "reqnCalId": CONSTANTS.HiringEventDetailsVO.modifyAvailabilityList.reqnCalId,
                            "moveDeleteAvailability": CONSTANTS.HiringEventDetailsVO.modifyAvailabilityList.moveDeleteAvailability,
                            "daysBetweenOriginalStartDateAndNewStartDate": CONSTANTS.HiringEventDetailsVO.modifyAvailabilityList.daysBetweenOriginalStartDateAndNewStartDate
                          }
                        };
            }
            //call back function to add
            var callbackFunction = $.Callbacks('once');
            callbackFunction
                    .add(CONSTANTS.retailStaffingReq.updateHiringEventEventResult);
            //service calll for updateHiringEvent
            this.updateHiringEventURL = CONSTANTS.updateHiringEventURL;
            RSASERVICES.updateHiringEvent(callbackFunction,
                    this.updateHiringEventURL, updateHiringRequest);
        }
        /*
         * check for returned errors
         */
        else if (data.Response.hasOwnProperty("error")) {
            // Display warning message
            var errorResultList = [];
            if (Array.isArray(data.Response.error)) {
                errorResultList = data.Response.error;
            } else {
                errorResultList = [ data.Response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(errorResultList);
            $("#editwarningpopup").modal("hide");
            $('#editAlertPopupModal .modal-body').addClass('autoHeight');
            $('#editAlertPopupModal .alertModalBody ').addClass('addAlertBodyHeight');
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(errorMessage);
        }
    };
    /*
     * this function is the success handler for update hiring event.
     */
    this.updateHiringEventEventResult = function(data) {
        /*
         * if status is success show the prompt as hiring event updated
         */
        if (data.Response.status) {
            $("#editwarningpopup").modal("hide");
            $("#editHiringEventPopUp").modal("hide");
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Hiring Event updated");
            hiringEvent.loadData();
        }
        /*
         * check for returned errors
         */
        else if (data.Response.hasOwnProperty("error")) {
            // Display warning message
            var errorResultList = [];
            if (Array.isArray(data.Response.error)) {
                errorResultList = data.Response.error;
            } else {
                errorResultList = [ data.Response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(errorResultList);
            $("#editwarningpopup").modal("hide");
            $('#editAlertPopupModal .modal-body').addClass('autoHeight');
            $('#editAlertPopupModal .alertModalBody ').addClass('addAlertBodyHeight');
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(errorMessage);
        }
    };

    /*
     * delete selected store from Participating Store List
     */
    this.deleteStoreFromList = function() {
        /*
         * the selected store eventsiteflag is y then it is the creater of the
         * event.so we cannot delete it
         */
        if (CONSTANTS.selectedStoreSummary.eventSiteFlg === "Y") {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(
                            "Store "
                                    + CONSTANTS.selectedStoreSummary.number
                                    + " is the location of the Event, therefore it cannot be deleted.");
            editHiringEvent.addEditModalCss();
            return;
        }
        /*
         * if store list length is one we cannot delete
         */
        if (CONSTANTS.HiringEventStoreList.length === 1) {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody")
                    .html(
                            "There has to be at least one Participating Store in a Hiring Event");
            editHiringEvent.addEditModalCss();
            return;
        }
        CONSTANTS.editWarningPopupFlag = true;
        $('#editwarningpopup .modal-body').removeClass('addHeight');
        $("#editwarningpopup #WarningModalLabel").text("Warning");
        $("#editwarningpopup .cautionMsg").html(
                "CAUTION, you are about to delete store '"
                        + CONSTANTS.selectedStoreSummary.number +".'");
        $("#editwarningpopup .sureMsg").html("Are You Sure?");
        $("#editwarningpopup #oldRenameCalName").html("");
        this.setDraggableForWarning();
        $("#editwarningpopup").modal('show');
        // CAUTION, you are about to delete store
    };
    /*
     * this function is called when we delete a store from the participating
     * store list and click yes for warning pop up
     */
    this.warnYesClicked = function() {
        $("#editwarningpopup").modal("hide");
        var count = 0;
        for ( var i = 0; i < CONSTANTS.HiringEventStoreList.length; i++) {
            if (CONSTANTS.selectedStoreSummary.number === CONSTANTS.HiringEventStoreDetailsVO[i].number) {
                MODEL.counter = count;
                if (CONSTANTS.selectedStoreSummary.hireEventId !== 0
                        && CONSTANTS.selectedStoreSummary.hireEventId !== undefined) {
                    var callbackFunction = $.Callbacks('once');
                    this.deleteHiringEventParticipatingStoreURL = CONSTANTS.deleteHiringEventParticipatingStoreURL;
                    callbackFunction.add(this.deleteSuccessOnStore);
                    var deleteStorerequest = {
                        "DeleteParticipatingStoreRequest" : {
                            "storeNum" : CONSTANTS.selectedStoreSummary.number,
                            "hireEventId" : CONSTANTS.selectedStoreSummary.hireEventId

                        }
                    };
                    /*
                     * Service call for deleteHiring participating
                     * store for delete request
                     */
                    RSASERVICES.deleteHiringEventParticipatingStore(
                            callbackFunction,
                            this.deleteHiringEventParticipatingStoreURL,
                            deleteStorerequest);
                } else {
                    MODEL.hiringEventDetail.participatingStores
                            .splice(count, 1);
                    CONSTANTS.retailStaffingReq
                            .buildStoresGrid(MODEL.hiringEventDetail.participatingStores);
                }
            }
            count++;
        }
    };
    /*
     * Method is used the success handler for delete participating store where
     * it delete the store from the list and grid
     */
    this.deleteSuccessOnStore = function(data) {
        var returnedErrorMessage = "";
        //check the response has error
        if (data.Response.hasOwnProperty("error")) {
            if (_.isArray(data.Response.error)) {
                returnedErrorMessage = data.Response.error;
            } else {
                returnedErrorMessage = [ data.Response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(returnedErrorMessage);
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $('#editAlertPopupModal .modal-body').addClass('autoHeight');
            $("#editAlertPopupModal .alertModalBody")
                    .html(errorMessage);
        } else if (data.Response.hasOwnProperty("status")
                && data.Response.status === "SUCCESS") {
            $('#editAlertPopupModal').modal('show');            
            $("#editAlertPopupModal .alertModalBody").html(
                    "participating stores deleted");
            MODEL.hiringEventDetail.participatingStores
                    .splice(MODEL.counter, 1);
            //Method call on success of delete
            CONSTANTS.retailStaffingReq
                    .buildStoresGrid(MODEL.hiringEventDetail.participatingStores);
        }
    };
    /*
     * Method is used to get store details for an
     * in-store hiring event
     */
    this.getStoreInfo = function(event) {
        //check the length is 4
        if (eventStrNum.val().length === 4) {
            var callbackFunction = $.Callbacks('once');
            this.addHiringEventStoresURL = CONSTANTS.addHiringEventStoresURL;
            //service callback for addHiringEventStore
            callbackFunction.add(this.getHiringEventStoreDetailsResult);
            RSASERVICES.addHiringEventStores(callbackFunction,
                    this.addHiringEventStoresURL, eventStrNum.val(), true,
                    "Please wait...");
            /*
             * Add to Participating Stores
             */
            enterStrNum.val(eventStrNum.val());
            this.locationChange = true;
            this.addStoreToHiringEvent(event);
            this.setDirtyPage();
        } else {
            /*
             * Empty the fields if not met the condition
             */
            eventAddress.empty();
            eventCity.empty();
            eventZip.empty();
            eventState.empty();
            venueName.val("");
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
                hiringEventStoreDetailsVO.number = data.storeResponse.storeDetails.number;
                hiringEventStoreDetailsVO.name = data.storeResponse.storeDetails.name;
                hiringEventStoreDetailsVO.address1 = data.storeResponse.storeDetails.addressLine1;
                hiringEventStoreDetailsVO.city = data.storeResponse.storeDetails.city;
                hiringEventStoreDetailsVO.state = data.storeResponse.storeDetails.state;
                hiringEventStoreDetailsVO.zip = data.storeResponse.storeDetails.postalCode;
                CONSTANTS.HiringEventStoreDetailsVO = CONSTANTS.HiringEventStoreList;
                eventAddress.text(hiringEventStoreDetailsVO.address1);
                eventState.text(hiringEventStoreDetailsVO.state);
                eventCity.text(hiringEventStoreDetailsVO.city);
                eventZip.text(hiringEventStoreDetailsVO.zip);
            } else if (!data.storeResponse.valid) {
                CONSTANTS.retailStaffingReq.setFocusandDraggable();
                $("#editAlertPopupModal .alertModalBody").html(
                        "Store/Location Number " + eventStrNum.val()
                                + " is invalid.");
                editHiringEvent.addEditModalCss();
                $("#editAlertPopupModal .modal-body ").removeClass(
                        "editHiringEventlargeBody");
                $("#editAlertPopupModal .alertModalBody").removeClass(
                        "editHiringEventalignModal");
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
            $('#editAlertPopupModal .modal-body').addClass('autoHeight');
            $("#editAlertPopupModal .alertModalBody")
                    .html(errorMessage);
        }
    };
    /*
     * this function is called when refresh button is clicked and hit the
     * service to get manager details
     */
    this.refreshManagerData = function() {
        /*
         * if manager text box is empty shows the prompt
         */
        if (eventMgrLdap.val() === "") {
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            $("#editAlertPopupModal .alertModalBody").html(
                    "Please enter a LDAP ID.");
            return;
        } else {
            /*
             * Empty the fields if not met the condition
             */
            CONSTANTS.hiringEventMgrData = {};
            $("#editMgrName").empty();
            $("#editMgrTitle").empty();
            $("#editMgrPhn").empty();
            $("#editMgrMail").empty();
        }
        this.phoneNumberChange = true;
        this.setDirtyPage();
        var callbackFunction = $.Callbacks('once');
        this.getHiringEventMgrDataURL = CONSTANTS.getHiringEventMgrDataURL;
        //service call back for manger detail.
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
         * Clear Hiring Event Manager Data.
         */
        CONSTANTS.hiringEventMgrData = {};
        // valid response
        if (data.Response.hasOwnProperty("hiringEventResponse") && data.Response.hiringEventResponse
                .hasOwnProperty("hiringEventMgrData")) {
            if (data.Response.hiringEventResponse.hiringEventMgrData != null) {
                /*
                 * Assign all values if above condition true then get data from
                 * response and add in to CONSTANTS.hiringEventMgrData array
                 */
                CONSTANTS.hiringEventMgrData.eventMgrName = data.Response.hiringEventResponse.hiringEventMgrData.name;
                CONSTANTS.hiringEventMgrData.eventMgrTitle = data.Response.hiringEventResponse.hiringEventMgrData.title;
                CONSTANTS.hiringEventMgrData.eventMgrPhone = data.Response.hiringEventResponse.hiringEventMgrData.phone;
                CONSTANTS.hiringEventMgrData.eventMgrEmail = data.Response.hiringEventResponse.hiringEventMgrData.email;
                CONSTANTS.hiringEventMgrData.eventMgrAssociateId = data.Response.hiringEventResponse.hiringEventMgrData.associateId;
                //set the data to all respective manager fields.
                $("#editMgrName").text(CONSTANTS.hiringEventMgrData.eventMgrName);
                $("#editMgrTitle").text(CONSTANTS.hiringEventMgrData.eventMgrTitle);
                $("#editMgrPhn").text(CONSTANTS.hiringEventMgrData.eventMgrPhone);
                $("#editMgrMail").text(CONSTANTS.hiringEventMgrData.eventMgrEmail);
            }
        }
        // Check for returned errors
        else if (data.Response.hasOwnProperty("error")) {
            if (_.isArray(data.Response.error)) {
                returnedErrorMessage = data.Response.error.errorMsg;
            } else {
                returnedErrorMessage = [ data.Response.error.errorMsg ];
            }
            CONSTANTS.retailStaffingReq.setFocusandDraggable();
            
            $("#editAlertPopupModal .alertModalBody")
                    .html(returnedErrorMessage);
            // Clear Hiring Event Manager Data.
            CONSTANTS.hiringEventMgrData = {};
        }
    };
    /*
     * Sets the selected range for Hiring Event End Date to start at the Begin
     * Date and greater.
     */
    this.setEndDate = function() {
        this.setDirtyPage();
        var minDate;
        if (eventDt.val() !== "") {
            minDate = new Date(eventDt.val().substring(6), (eventDt.val()
                    .substring(0, 2)) - 1, eventDt.val().substring(3, 2));
            this.endDateBeginRange = minDate;
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
     * this function is used to close the popup when cancel is clicked to show
     * the warning popup
     */
    this.closePopup = function() {
        if (MODEL.dirtyPage) {
             $("#editwarningpopup .modal-dialog").removeClass("editWarningWidth");
            $('#editwarningpopup .modal-body').removeClass('addHeight');
            $("#editwarningpopup #WarningModalLabel").text("Warning");
            $(".cautionMsg")
                    .html(
                            "CAUTION, you have made changes to the Hiring Event, changes will be lost.");
            $("#editwarningpopup #oldRenameCalName").html("");

            $(".sureMsg").html("Are You Sure?");
            this.setDraggableForWarning();
            $("#editwarningpopup").modal('show');
            MODEL.closeFlag = true;
        } else {
            $("#editHiringEventPopUp").modal("hide");
        }
        hiringEvent.loadData();
    };
    /*
     * this function is used to close alert popup and remove css for that popups
     */
    this.Okclicked = function() {
        $('#editAlertPopupModal .modal-body').removeClass('addAlertHeight');
        $('#editAlertPopupModal .alertModalBody ').removeClass('addAlertBodyHeight');
        $("#editAlertPopupModal").modal("hide");
        $("#editHiringEventPopUp .modal-body").scrollTop(0);
        if (this.editLocked) {
            $(".eventLocDtls").addClass("opacityDiv");
            $(".partStores").addClass("opacityDiv");
            $(".opacityDiv input").attr("disabled", "disabled");
            $(".opacityDiv #editrefreshMgrData").attr("disabled", "disabled");
             $("#editVenueStateCbo").attr("disabled", "disabled");
            if($("#editVenueStateCbo").data("selectBox-selectBoxIt")){
                $("#editVenueStateCbo").data("selectBox-selectBoxIt").refresh();
                }
            $( "#editeventDt,#editeventDtEnd" ).datepicker( "option", "disabled", true );
            $(".storeDelBtn").attr("disabled", "disabled");
            $("#create").attr("disabled","disabled");
        }
    };
    /*
     * Method has css class to remove
     * for createAlertPopupModal popup
     */
    this.removeEditAlertCss = function() {
        $("#editAlertPopupModal .modal-content").removeClass(
                "editHiringEventlargeModal");
        $("#editAlertPopupModal .alertModalBody").removeClass(
                "editHiringEventalignModal");
        $("#editAlertPopupModal .modal-body ").removeClass(
                "editHiringEventlargeBody");
        $('#editAlertPopupModal .alertModalBody ').removeClass('addAlertBodyHeight');
        $("#editwarningpopup .modal-dialog").removeClass("editWarningWidth");
        $('#editAlertPopupModal .modal-body').addClass('autoHeight');
    };
    /*
     * Method has css class to add
     * for createAlertPopupModal popup
     */
    this.addEditModalCss = function() {
        $("#editAlertPopupModal .modal-content").addClass(
                "editHiringEventlargeModal");
        $("#editAlertPopupModal .alertModalBody").addClass(
                "editHiringEventalignModal");
        $("#editAlertPopupModal .modal-body ").addClass(
                "editHiringEventlargeBody");
        $('#editAlertPopupModal').on('hidden.bs.modal', function() {
            editHiringEvent.removeEditAlertCss();
        });
    };
    /*
     * Method is used to convert and form the date
     * MM/DD/YYYY
     */
    this.convertEditDate = function(inDate) {
        return inDate.substr(5, 2) + "/" + inDate.substr(8, 2) + "/"
                + inDate.substr(0, 4);
    };
    /*
     * Method is used to show and drag the
     * editwarningpopup popup.
     */
    this.setDraggableForWarning = function(){
         this.blockPopup('#editwarningpopup');
         $("#editwarningpopup").draggable({
              handle: ".modal-header"
          });
    };
    /*
     * Method is used to show and drag the
     * editAlertPopupModal popup.
     */
    this.setFocusandDraggable = function(){
        $('#editAlertPopupModal').off("shown.bs.modal");
        $('#editAlertPopupModal').on("shown.bs.modal", function() {
            $("#createHiringButton").focus();
        });
        CONSTANTS.retailStaffingReq.blockPopup('#editAlertPopupModal');
        CONSTANTS.retailStaffingReq.removeEditAlertCss();
        $("#editAlertPopupModal").draggable({
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